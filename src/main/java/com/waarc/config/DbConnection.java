package com.waarc.config;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Properties;

/**
 * @author <sachin.singh@moco.com.np>
 * @created on : 15-02-2026 19:42
 */
public class DbConnection {
    private static interface Singleton {

        final DbConnection INSTANCE = new DbConnection();
    }

    private final BasicDataSource dataSource;

    /**
     * This connection factory implements dbcp2 pooling. It creates instance of
     * BasicDataSource to access the DBCP pool. Closing a connection will simply
     * return it to its pool.
     */
    private DbConnection() {
        Properties dbProps = Config.getSectionProperties("db_");
        String server = dbProps.getProperty("db_server");
        String port = dbProps.getProperty("db_port");
        String dbName = dbProps.getProperty("db_name");
        String user = dbProps.getProperty("db_user");
        String password = dbProps.getProperty("db_password");
        StringBuilder url = new StringBuilder("jdbc:mysql://");
        url.append(server);
        url.append(":");
        url.append(port);
        url.append("/");
        url.append(dbName);
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(url.toString());
        ds.setUsername(user);
        ds.setPassword(password);

        ds.setInitialSize(Integer.parseInt(dbProps.getProperty("db_pool_initial_size").trim()));    //initial number of connections that are created when the pool is started.
        ds.setMaxTotal(Integer.parseInt(dbProps.getProperty("db_pool_max_size").trim()));   //maximum number of active connections (for all types) that can be allocated from this pool at the same time
        ds.setMaxIdle(Integer.parseInt(dbProps.getProperty("db_pool_max_idle_size").trim()));   //maximum number of active connections of each type (read-only|read-write) that can remain idle in the pool, without extra ones being released
        ds.setMinIdle(Integer.parseInt(dbProps.getProperty("db_pool_min_idle_size").trim()));   // minimum number of active connections of each type (read-only|read-write) that can remain idle in the pool, without extra ones being created
        ds.setMaxWait(Duration.ofMillis(Long.parseLong(dbProps.getProperty("db_pool_max_wait").trim())));    //maximum number of milliseconds that the pool will wait (when there are no available connections) for a connection to be returned before throwing an exception
        ds.setRemoveAbandonedTimeout(Duration.ofSeconds(Integer.parseInt(dbProps.getProperty("db_pool_remove_abandoned_timeout").trim()))); //Timeout in seconds before an abandoned connection can be removed
        ds.setRemoveAbandonedOnBorrow(Boolean.parseBoolean(dbProps.getProperty("db_pool_remove_abandoned_on_borrow").trim()));    //remove abandoned connections if they exceed the removeAbandonedTimout(default 300 sec).
        ds.setDurationBetweenEvictionRuns(Duration.ofMillis(Long.parseLong(dbProps.getProperty("db_pool_time_between_eviction_runs_millis").trim())));   //number of milliseconds to sleep between runs of the idle object evictor thread
        ds.setRemoveAbandonedOnMaintenance(Boolean.parseBoolean(dbProps.getProperty("db_pool_remove_abandoned_on_maintenance").trim())); // removeAbandonedOnMaintenance to true removes abandoned connections on the maintenance cycle (when eviction ends). This property has no effect unless maintenance is enabled by setting timeBetweenEvictionRunsMillis to a positive value.
        this.dataSource = ds;
    }

    /**
     * Provides database connection through pooled datasource.
     *
     * @return
     * @throws SQLException
     */
    public static synchronized Connection getCon() throws SQLException {
        return Singleton.INSTANCE.dataSource.getConnection();
    }

}