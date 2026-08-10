package com.robothaver.mp3reorder.mp3.controls.menubar;

import javafx.beans.property.*;
import lombok.Getter;

import java.util.Locale;

@Getter
public class MenuBarModel {
    private final ObjectProperty<Themes> selectedTheme = new SimpleObjectProperty<>();
    private final IntegerProperty selectedSize = new SimpleIntegerProperty();
    private final ObjectProperty<Locale> selectedLocale = new SimpleObjectProperty<>();
    private final BooleanProperty detailsMenuEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty statusBarEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty audioPlayerEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty launchMaximized = new SimpleBooleanProperty(false);
    private final BooleanProperty useSystemMenuBar = new SimpleBooleanProperty(true);
}
