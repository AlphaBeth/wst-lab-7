package ru.ifmo.wst.lab1.util;

import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.Properties;

/**
 * Reads properties from classpath
 */
public class Configuration {
    private Properties properties;

    @SneakyThrows
    public Configuration(String fileName) {
        this.properties = new Properties();
        try (InputStream is = Configuration.class.getClassLoader().getResourceAsStream(fileName)) {
            this.properties.load(is);
        }
    }

    public String get(String name) {
        return properties.getProperty(name);
    }

    public String get(String name, String defaultValue) {
        return properties.getProperty(name, defaultValue);
    }

}
