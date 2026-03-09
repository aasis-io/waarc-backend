package com.waarc.config;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Properties;

public class DbConnection {

    // Singleton holder pattern for lazy initialization
    private static class Singleton {
        private static final DbConnection INSTANCE = new DbConnection();
    }

    private final BasicDataSource dataSource;

    private DbConnection() {
        // Load DB properties from Config (env + .env)
        Properties dbProps = Config.getSectionProperties("DB_");

        String server = Config.getProperty("DB_SERVER");
        String port = Config.getProperty("DB_PORT");
        String dbName = Config.getProperty("DB_NAME");
        String user = Config.getProperty("DB_USER");
        String password = Config.getProperty("DB_PASSWORD");

        // fallback to defaults if missing
        server = (server == null || server.isBlank()) ? "localhost" : server.trim();
        port = (port == null || port.isBlank()) ? "3306" : port.trim();
        user = (user == null) ? "root" : user.trim();
        password = (password == null) ? "" : password.trim();

        if (dbName == null || dbName.isBlank()) {
            throw new IllegalStateException("Database name (DB_NAME) is required!");
        }

        // JDBC URL for MySQL
        String url = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8",
                server, port, dbName
        );

        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);

        // Pool configuration (with safe defaults)
        ds.setInitialSize(getInt(dbProps, "DB_POOL_INITIAL_SIZE", 5));
        ds.setMaxTotal(getInt(dbProps, "DB_POOL_MAX_SIZE", 20));
        ds.setMaxIdle(getInt(dbProps, "DB_POOL_MAX_IDLE_SIZE", 5));
        ds.setMinIdle(getInt(dbProps, "DB_POOL_MIN_IDLE_SIZE", 2));

        ds.setMaxWaitMillis(getLong(dbProps, "DB_POOL_MAX_WAIT", 9000));

        ds.setRemoveAbandonedTimeout(getInt(dbProps, "DB_POOL_REMOVE_ABANDONED_TIMEOUT", 180));
        ds.setRemoveAbandonedOnBorrow(getBoolean(dbProps, "DB_POOL_REMOVE_ABANDONED_ON_BORROW", true));
        ds.setRemoveAbandonedOnMaintenance(getBoolean(dbProps, "DB_POOL_REMOVE_ABANDONED_ON_MAINTENANCE", true));

        ds.setTimeBetweenEvictionRunsMillis(getLong(dbProps, "DB_POOL_TIME_BETWEEN_EVICTION_RUNS_MILLIS", 60000));

        this.dataSource = ds;
    }

    /** Get a connection from the pooled datasource */
    public static Connection getCon() throws SQLException {
        return Singleton.INSTANCE.dataSource.getConnection();
    }

    // -------- Helper methods --------
    private static int getInt(Properties p, String key, int defaultValue) {
        try {
            String v = Config.getProperty(key);
            if (v != null && !v.isBlank()) return Integer.parseInt(v.trim());
        } catch (Exception ignored) {}
        return defaultValue;
    }

    private static long getLong(Properties p, String key, long defaultValue) {
        try {
            String v = Config.getProperty(key);
            if (v != null && !v.isBlank()) return Long.parseLong(v.trim());
        } catch (Exception ignored) {}
        return defaultValue;
    }

    private static boolean getBoolean(Properties p, String key, boolean defaultValue) {
        String v = Config.getProperty(key);
        if (v != null && !v.isBlank()) return Boolean.parseBoolean(v.trim());
        return defaultValue;
    }
}