package com.waarc.config;

import com.waarc.log.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author <sachin.singh@moco.com.np>
 * @created on : 15-02-2026 09:18
 */
public class Config {

    private static final Logger LOG = LogManager.getLogger(Config.class);
    private static final String CONFIG_FILE = "etc/config.properties"; // optional fallback
    private static Properties props;

    /**
     * Load config from file if exists, otherwise rely on environment variables
     */
    private static void loadConfiguration() {
        props = new Properties();

        // First, try to load from file
        try (InputStream file = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (file != null) {
                props.load(file);
                LOG.info("Configuration loaded from classpath config.properties");
            } else {
                LOG.warn("config.properties not found in classpath, relying on environment variables");
            }
        } catch (IOException e) {
            Log log = new Log(Log.Status.FAILED, Log.Section.CONFIG,
                    "Error while loading configuration file : " + e.getMessage());
            LOG.error(log.toString());
        }
    }

    /**
     * Get property by key, first from environment variables, then from loaded properties
     */
    public static String getProperty(String key) {
        // Check environment variables first
        String envValue = System.getenv(key.toUpperCase().replace(".", "_"));
        if (envValue != null && !envValue.isEmpty()) {
            return envValue.trim();
        }

        // Fallback to properties file
        if (props == null || props.isEmpty()) {
            loadConfiguration();
        }

        String value = props.getProperty(key);
        if (value != null) {
            return value.trim();
        }

        LOG.warn("Property not found: " + key);
        return null;
    }

    /**
     * Get all properties for a section (optional)
     */
    public static Properties getSectionProperties(String section) {
        if (props == null || props.isEmpty()) {
            loadConfiguration();
        }

        Properties p = new Properties();
        props.forEach((key, value) -> {
            if (key.toString().contains(section)) {
                p.put(key, value);
            }
        });
        return p;
    }
}