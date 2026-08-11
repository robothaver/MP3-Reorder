package com.robothaver.mp3reorder.core.preference;

import com.robothaver.mp3reorder.core.ApplicationInfo;
import com.robothaver.mp3reorder.mp3.controls.menubar.Themes;
import lombok.extern.log4j.Log4j2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class PreferencesUtils {
    private PreferencesUtils() {
        /* This utility class should not be instantiated */
    }

    private static final String PREFERENCES_FILE_NAME = "preferences.properties";
    public static final Preferences DEFAULT_PREFERENCES = new Preferences(true, true, false, true, true, Themes.PRIMER_DARK, 14, ApplicationInfo.DEFAULT_LOCALE);
    public static final Path PREFERENCES_PATH = resolvePreferencesPath();

    private static Path resolvePreferencesPath() {
        String os = System.getProperty("os.name").toLowerCase();
        String dataDir;

        if (os.contains("mac")) {
            dataDir = System.getProperty("user.home") + "/Library/Application Support/MP3Reorder";
        } else if (os.contains("win")) {
            dataDir = System.getenv("APPDATA") + "/MP3Reorder";
        } else {
            dataDir = System.getProperty("user.home") + "/.MP3Reorder";
        }

        Path dirPath = Paths.get(dataDir);
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
            } catch (Exception e) {
                log.error("Failed to create preferences directory", e);
            }
        }

        return dirPath.resolve(PREFERENCES_FILE_NAME);
    }
}
