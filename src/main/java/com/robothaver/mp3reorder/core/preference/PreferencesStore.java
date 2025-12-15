package com.robothaver.mp3reorder.core.preference;

public interface PreferencesStore<T> {
    T getPreferences();
    void savePreferences();
}
