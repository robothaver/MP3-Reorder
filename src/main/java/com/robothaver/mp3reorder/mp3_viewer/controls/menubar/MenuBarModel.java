package com.robothaver.mp3reorder.mp3_viewer.controls.menubar;

import com.robothaver.mp3reorder.core.ApplicationInfo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

import java.util.Locale;

@Getter
public class MenuBarModel {
    private final ObjectProperty<Themes> selectedTheme = new SimpleObjectProperty<>(Themes.PRIMER_DARK);
    private final ObjectProperty<Size> selectedSize = new SimpleObjectProperty<>(Size.AUTO);
    private final ObjectProperty<Locale> selectedLocale = new SimpleObjectProperty<>(ApplicationInfo.DEFAULT_LOCALE);
}
