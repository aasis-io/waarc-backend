package com.waarc.config;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Properties;

public class DbConnection {

    private static class Singleton {
        private static final DbConnection INSTANCE = new DbConnection();
    }

    private final BasicDataSource dataSource;

    private DbConnection() {

        Properties dbProps = Config.getSectionProperties("db_");

        String server = get(dbProps, "db_server", "localhost");
        String port = get(dbProps, "db_port", "3306");
        String dbName = get(dbProps, "db_name", "");
        String user = get(dbProps, "db_user", "root");
        String password = get(dbProps, "db_password", "");

        String url =
                "jdbc:mysql://" + server + ":" + port + "/" + dbName +
                        "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";

        BasicDataSource ds = new BasicDataSource();

        // Driver
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // DB connection
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);

        // Pool configuration
        ds.setInitialSize(getInt(dbProps, "db_pool_initial_size", 5));
        ds.setMaxTotal(getInt(dbProps, "db_pool_max_size", 20));
        ds.setMaxIdle(getInt(dbProps, "db_pool_max_idle_size", 5));
        ds.setMinIdle(getInt(dbProps, "db_pool_min_idle_size", 2));

        ds.setMaxWait(Duration.ofMillis(getLong(dbProps, "db_pool_max_wait", 9000)));

        ds.setRemoveAbandonedTimeout(
                Duration.ofSeconds(getInt(dbProps, "db_pool_remove_abandoned_timeout", 180))
        );

        ds.setRemoveAbandonedOnBorrow(
                Boolean.parseBoolean(get(dbProps, "db_pool_remove_abandoned_on_borrow", "true"))
        );

        ds.setRemoveAbandonedOnMaintenance(
                Boolean.parseBoolean(get(dbProps, "db_pool_remove_abandoned_on_maintenance", "true"))
        );

        ds.setDurationBetweenEvictionRuns(
                Duration.ofMillis(getLong(dbProps, "db_pool_time_between_eviction_runs_millis", 60000))
        );

        this.dataSource = ds;
    }

    public static Connection getCon() throws SQLException {
        return Singleton.INSTANCE.dataSource.getConnection();
    }

    // -------- helpers --------

    private static String get(Properties p, String key, String defaultValue) {
        String v = p.getProperty(key);
        return (v == null || v.isBlank()) ? defaultValue : v.trim();
    }

    private static int getInt(Properties p, String key, int defaultValue) {
        try {
            return Integer.parseInt(get(p, key, String.valueOf(defaultValue)));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static long getLong(Properties p, String key, long defaultValue) {
        try {
            return Long.parseLong(get(p, key, String.valueOf(defaultValue)));
        } catch (Exception e) {
            return defaultValue;
        }
    }
}