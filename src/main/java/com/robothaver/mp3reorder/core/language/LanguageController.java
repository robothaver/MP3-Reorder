package com.robothaver.mp3reorder.core.language;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

import java.util.Locale;

public class LanguageController {
    @Getter
    private static final ObjectProperty<Locale> selectedLocale = new SimpleObjectProperty<>();

    private LanguageController() {
        throw new IllegalStateException("Static class");
    }

    public static void changeSelectedLocale(Locale locale) {
        selectedLocale.setValue(locale);
    }
}
