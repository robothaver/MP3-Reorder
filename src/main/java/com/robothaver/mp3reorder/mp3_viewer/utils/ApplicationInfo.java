package com.robothaver.mp3reorder.mp3_viewer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationInfo {
    public static final String APPLICATION_NAME;
    public static final String APPLICATION_VERSION;

    static {
        Properties properties = new Properties();
        try (InputStream resource = Utils.class.getResourceAsStream("/application.properties")) {
            properties.load(resource);
            APPLICATION_NAME = properties.getProperty("application.name");
            APPLICATION_VERSION = properties.getProperty("application.version");
        } catch (IOException e) {
            throw new IllegalStateException("No properties file found!");
        }
    }

    private ApplicationInfo() {
        throw new IllegalStateException("Utility class");
    }
}
