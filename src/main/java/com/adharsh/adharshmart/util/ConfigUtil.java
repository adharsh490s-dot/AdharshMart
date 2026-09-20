package com.adharsh.adharshmart.util;

import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (Exception ignored) {}
    }

    public static String get(String key, String defaultValue) {
        String envValue = System.getenv(key.replace('.', '_').toUpperCase());
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        return properties.getProperty(key, defaultValue);
    }
}