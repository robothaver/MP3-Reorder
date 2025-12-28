package com.robothaver.mp3reorder.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class ApplicationInfo {
    public static final String APPLICATION_NAME;
    public static final String APPLICATION_VERSION;
    public static final List<Locale> SUPPORTED_LOCALES;
    public static final Locale DEFAULT_LOCALE;

    static {
        Properties properties = new Properties();
        try (InputStream resource = ApplicationInfo.class.getResourceAsStream("/application.properties")) {
            properties.load(resource);
            APPLICATION_NAME = properties.getProperty("application.name");
            APPLICATION_VERSION = properties.getProperty("application.version");
            SUPPORTED_LOCALES = Arrays.stream(properties.getProperty("application.supported.locales").split(","))
                    .map(Locale::forLanguageTag)
                    .toList();
            DEFAULT_LOCALE = Locale.forLanguageTag(properties.getProperty("application.default.locale"));
        } catch (IOException _) {
            throw new IllegalStateException("No properties file found!");
        }
    }

    private ApplicationInfo() {
        throw new IllegalStateException("Utility class");
    }
}
