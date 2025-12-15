package com.robothaver.mp3reorder.core.preference;

import com.robothaver.mp3reorder.core.ApplicationInfo;
import com.robothaver.mp3reorder.mp3.controls.menubar.Size;
import com.robothaver.mp3reorder.mp3.controls.menubar.Themes;

import java.util.Locale;
import java.util.Properties;

public class PreferencesUtils {

    private PreferencesUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Preferences getDefaultPreferences() {
        return new Preferences(true, true, Themes.PRIMER_DARK, Size.AUTO, ApplicationInfo.DEFAULT_LOCALE);
    }


}
