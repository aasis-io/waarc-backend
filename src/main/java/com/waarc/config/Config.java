package com.waarc.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Properties;

public class Config {

    private static final Logger LOG = LogManager.getLogger(Config.class);

    // Load .env file once
    private static final Dotenv DOTENV = Dotenv.configure()
            .ignoreIfMissing() // don’t fail if .env missing
            .load();

    /**
     * Get property by key.
     * Priority:
     * 1️⃣ Environment variables (uppercase, underscores)
     * 2️⃣ .env file fallback
     */
    public static String getProperty(String key) {
        // Convert dots to underscores and uppercase for env vars
        String envKey = key.toUpperCase().replace(".", "_");
        String envValue = System.getenv(envKey);

        if (envValue != null && !envValue.isBlank()) {
            return envValue.trim();
        }

        // fallback to .env
        String dotEnvValue = DOTENV.get(envKey);
        if (dotEnvValue != null && !dotEnvValue.isBlank()) {
            return dotEnvValue.trim();
        }

        LOG.warn("Property not found: {}", key);
        return null;
    }

    /**
     * Get all properties of a section (e.g., "DB_" prefix) from environment variables or .env.
     */
    public static Properties getSectionProperties(String sectionPrefix) {
        Properties sectionProps = new Properties();

        // 1️⃣ From OS env
        System.getenv().forEach((envKey, envValue) -> {
            if (envKey.startsWith(sectionPrefix.toUpperCase())) {
                sectionProps.put(envKey.toLowerCase(), envValue);
            }
        });

        // 2️⃣ From .env fallback
        DOTENV.entries().forEach(entry -> {
            String envKey = entry.getKey();
            String envValue = entry.getValue();
            if (envKey.startsWith(sectionPrefix.toUpperCase()) && !sectionProps.containsKey(envKey.toLowerCase())) {
                sectionProps.put(envKey.toLowerCase(), envValue);
            }
        });

        if (sectionProps.isEmpty()) {
            LOG.warn("No properties found for section: {}", sectionPrefix);
        }

        return sectionProps;
    }
}