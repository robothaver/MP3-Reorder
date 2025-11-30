package com.robothaver.mp3reorder.mp3_viewer;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;

import java.util.Locale;
import java.util.ResourceBundle;

public class ViewLocalization {
    private final String resourceBundlePath;
    private final ObjectProperty<Locale> locale;
    private ResourceBundle resourceBundle;

    public ViewLocalization(String resourceBundlePath, ObjectProperty<Locale> locale) {
        this.resourceBundlePath = resourceBundlePath;
        this.locale = locale;
        resourceBundle = ResourceBundle.getBundle(resourceBundlePath, locale.get());
    }

    public String getForKey(String key) {
        tryUpdateResourceBundle();
        return resourceBundle.getString(key);
    }

    public StringBinding bindString(String key) {
        return Bindings.createStringBinding(() -> {
            tryUpdateResourceBundle();
            return resourceBundle.getString(key);
        }, locale);
    }

    private void tryUpdateResourceBundle() {
        if (!resourceBundle.getLocale().equals(locale.get())) {
            resourceBundle = ResourceBundle.getBundle(resourceBundlePath, locale.get());
        }
    }
}
