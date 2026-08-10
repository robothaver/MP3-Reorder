package com.robothaver.mp3reorder.core.preference;

import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.mp3.controls.menubar.Themes;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import lombok.extern.log4j.Log4j2;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Properties;

import static com.robothaver.mp3reorder.core.preference.PreferencesUtils.DEFAULT_PREFERENCES;
import static com.robothaver.mp3reorder.core.preference.PreferencesUtils.PREFERENCES_PATH;

@Log4j2
public class PreferenceStoreImpl implements PreferencesStore<Preferences> {
    private static final PreferencesStore<Preferences> instance = new PreferenceStoreImpl();
    private static final ViewLocalization localization = new ViewLocalization("language.preferences", new SimpleObjectProperty<>(DEFAULT_PREFERENCES.getSelectedLocale()));
    private static Preferences preferences;

    public static PreferencesStore<Preferences> getInstance() {
        if (preferences == null) loadPreferences();
        return instance;
    }

    private static void loadPreferences() {
        Properties properties = new Properties();
        if (Files.exists(PREFERENCES_PATH)) {
            preferences = new Preferences();
            try (FileInputStream inputStream = new FileInputStream(PREFERENCES_PATH.toFile())) {
                properties.load(inputStream);
                loadFromProperties(properties, preferences);
            } catch (Exception e) {
                log.error("Failed to load preferences, reverting to default configuration", e);
                DialogManagerImpl.getInstance().showAlert(Alert.AlertType.ERROR, localization.getForKey("loading.error.dialog.title"), localization.getForKey("loading.error.dialog.message") + e);
                preferences = DEFAULT_PREFERENCES;
            }
        } else {
            Preferences defaultPreferences = DEFAULT_PREFERENCES;
            writeToProperties(properties, defaultPreferences);
            saveProperties(properties);
            preferences = defaultPreferences;
        }
    }

    private static void saveProperties(Properties properties) {
        try (FileWriter fileWriter = new FileWriter(PREFERENCES_PATH.toFile())) {
            properties.store(fileWriter, "App preferences");
        } catch (IOException e) {
            log.error("Failed to save properties", e);
        }
    }

    private static void loadFromProperties(Properties properties, Preferences preferences) {
        preferences.setSelectedTheme(Themes.fromString(properties.getProperty("theme")));
        preferences.setSelectedSize(Integer.parseInt(properties.getProperty("size")));
        preferences.setSelectedLocale(Locale.forLanguageTag(properties.getProperty("locale")));
        preferences.setSideMenuEnabled(Boolean.parseBoolean(properties.getProperty("sideMenuEnabled")));
        preferences.setStatusBarEnabled(Boolean.parseBoolean(properties.getProperty("statusBarEnabled")));
        preferences.setLaunchMaximized(Boolean.parseBoolean(properties.getProperty("launchMaximized")));
        preferences.setUseSystemMenuBar(Boolean.parseBoolean(properties.getProperty("useSystemMenuBar")));
        preferences.setAudioPlayerEnabled(Boolean.parseBoolean(properties.getProperty("audioPlayerEnabled")));
    }

    private static void writeToProperties(Properties properties, Preferences preferences) {
        properties.setProperty("theme", preferences.getSelectedTheme().toString());
        properties.setProperty("size", String.valueOf(preferences.getSelectedSize()));
        properties.setProperty("locale", preferences.getSelectedLocale().toLanguageTag());
        properties.setProperty("sideMenuEnabled", String.valueOf(preferences.isSideMenuEnabled()));
        properties.setProperty("statusBarEnabled", String.valueOf(preferences.isStatusBarEnabled()));
        properties.setProperty("launchMaximized", String.valueOf(preferences.isLaunchMaximized()));
        properties.setProperty("useSystemMenuBar", String.valueOf(preferences.isUseSystemMenuBar()));
        properties.setProperty("audioPlayerEnabled", String.valueOf(preferences.isAudioPlayerEnabled()));
    }

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
}
