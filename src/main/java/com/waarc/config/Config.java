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
    private static final String CONFIG_FILE = "etc/config.properties";
    private static Properties props;

    /**
     * load config.properties file
     */
    private static void loadConfiguration() {
        Log log;
        try (InputStream file = new FileInputStream(CONFIG_FILE)) {
            props = new Properties();
            props.load(file);
        } catch (IOException e) {
            log = new Log(Log.Status.FAILED, Log.Section.CONFIG, "Error while loading " +
                    "configuration file : " + e.getMessage());
            LOG.debug(log.toString());
            log = new Log(Log.Status.FAILED, Log.Section.CONFIG, "Error loading configuration" +
                    "file");
            LOG.error(log.toString());
        }
    }

    public static Properties getSectionProperties(String section) {

        if (props == null || props.isEmpty()) {
            loadConfiguration();
        }
        final Properties p = new Properties();
        props.forEach((key, value) -> {
            if (key.toString().contains(section)) {
                p.put(key, value);
            }
        });
        return p;
    }


    public static String getProperty(String property) {

        if (props == null || props.isEmpty()) {
            loadConfiguration();
        }
        return props.getProperty(property).trim();
    }
}