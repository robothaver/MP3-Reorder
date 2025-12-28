package com.robothaver.mp3reorder.core.preference;

import com.robothaver.mp3reorder.core.font.Size;
import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.mp3.controls.menubar.Themes;
import javafx.scene.control.Alert;
import lombok.extern.log4j.Log4j2;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;

@Log4j2
public class PreferenceStoreImpl implements PreferencesStore<Preferences> {
    private static final PreferencesStore<Preferences> instance = new PreferenceStoreImpl();
    private static final String PREFERENCES_FILE_NAME = "preferences.properties";
    private static final Path PREFERENCES_PATH = Paths.get(PREFERENCES_FILE_NAME);

    private static Preferences preferences;

    @Override
    public Preferences getPreferences() {
        return preferences;
    }

    @Override
    public void savePreferences() {
        Properties properties = new Properties();
        writeToProperties(properties, preferences);
        saveProperties(properties);
    }

    public static PreferencesStore<Preferences> getInstance() {
        if (preferences == null) loadPreferences();
        return instance;
    }

    private static void loadPreferences() {
        Properties properties = new Properties();
        if (Files.exists(PREFERENCES_PATH)) {
            preferences = new Preferences();
            try (FileInputStream inputStream = new FileInputStream(PREFERENCES_FILE_NAME)) {
                properties.load(inputStream);
                loadFromProperties(properties, preferences);
            } catch (Exception e) {
                log.error("Failed to load preferences, reverting to default configuration", e);
                preferences = PreferencesUtils.DEFAULT_PREFERENCES;
            }
        } else {
            Preferences defaultPreferences = PreferencesUtils.DEFAULT_PREFERENCES;
            writeToProperties(properties, defaultPreferences);
            saveProperties(properties);
            preferences = defaultPreferences;
        }
    }

    private static void saveProperties(Properties properties) {
        try (FileWriter fileWriter = new FileWriter(PREFERENCES_PATH.toFile())) {
            properties.store(fileWriter, "App preferences");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void loadFromProperties(Properties properties, Preferences preferences) {
        preferences.setSelectedTheme(Themes.fromString(properties.getProperty("theme")));
        preferences.setSelectedSize(Size.fromString(properties.getProperty("size")));
        preferences.setSelectedLocale(Locale.forLanguageTag(properties.getProperty("locale")));
        preferences.setSideMenuEnabled(Boolean.parseBoolean(properties.getProperty("sideMenuEnabled")));
        preferences.setStatusBarEnabled(Boolean.parseBoolean(properties.getProperty("statusBarEnabled")));
    }

    private static void writeToProperties(Properties properties, Preferences preferences) {
        properties.setProperty("theme", preferences.getSelectedTheme().toString());
        properties.setProperty("size", preferences.getSelectedSize().toString());
        properties.setProperty("locale", preferences.getSelectedLocale().toLanguageTag());
        properties.setProperty("sideMenuEnabled", String.valueOf(preferences.isSideMenuEnabled()));
        properties.setProperty("statusBarEnabled", String.valueOf(preferences.isStatusBarEnabled()));
    }
}
