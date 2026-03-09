package com.waarc.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Logger LOG = LogManager.getLogger(Config.class);

    private static final String CLASSPATH_CONFIG = "config.properties";
    private static final String FILESYSTEM_CONFIG = "etc/config.properties";

    private static Properties props;

    private static void loadConfiguration() {

        props = new Properties();

        // 1️⃣ Try classpath (src/main/resources)
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream(CLASSPATH_CONFIG)) {

            if (in != null) {
                props.load(in);
                LOG.info("Configuration loaded from classpath: {}", CLASSPATH_CONFIG);
                return;
            }

        } catch (IOException e) {
            LOG.error("Failed loading classpath config: {}", e.getMessage());
        }

        // 2️⃣ Try filesystem (etc/config.properties)
        try (InputStream in = new FileInputStream(FILESYSTEM_CONFIG)) {

            props.load(in);
            LOG.info("Configuration loaded from file system: {}", FILESYSTEM_CONFIG);

        } catch (IOException e) {
            LOG.warn("No configuration file found. Falling back to environment variables.");
        }
    }

    public static String getProperty(String key) {

        // 1️⃣ Environment variable override
        String envKey = key.toUpperCase().replace(".", "_");
        String envValue = System.getenv(envKey);

        if (envValue != null && !envValue.isBlank()) {
            return envValue.trim();
        }

        // 2️⃣ Load config if not loaded
        if (props == null) {
            loadConfiguration();
        }

        // 3️⃣ Get from properties
        String value = props.getProperty(key);

        if (value != null) {
            return value.trim();
        }

        LOG.warn("Property not found: {}", key);
        return null;
    }

    public static Properties getSectionProperties(String section) {

        if (props == null) {
            loadConfiguration();
        }

        Properties sectionProps = new Properties();

        for (String key : props.stringPropertyNames()) {
            if (key.contains(section)) {
                sectionProps.put(key, props.getProperty(key));
            }
        }

        return sectionProps;
    }
}