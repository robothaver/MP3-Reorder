package com.robothaver.mp3reorder.core.preference;

import com.robothaver.mp3reorder.core.ApplicationInfo;
import com.robothaver.mp3reorder.core.font.Size;
import com.robothaver.mp3reorder.mp3.controls.menubar.Themes;

public class PreferencesUtils {
    public static final Preferences DEFAULT_PREFERENCES = new Preferences(true, true, Themes.PRIMER_DARK, Size.AUTO, ApplicationInfo.DEFAULT_LOCALE);

    private PreferencesUtils() {
        throw new IllegalStateException("Utility class");
    }
}
