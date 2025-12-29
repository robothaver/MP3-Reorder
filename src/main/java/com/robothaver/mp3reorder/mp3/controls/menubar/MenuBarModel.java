package com.robothaver.mp3reorder.mp3.controls.menubar;

import com.robothaver.mp3reorder.core.font.Size;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

import java.util.Locale;

@Getter
public class MenuBarModel {
    private final ObjectProperty<Themes> selectedTheme = new SimpleObjectProperty<>();
    private final ObjectProperty<Size> selectedSize = new SimpleObjectProperty<>();
    private final ObjectProperty<Locale> selectedLocale = new SimpleObjectProperty<>();
    private final BooleanProperty detailsMenuEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty statusBarEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty launchMaximized = new SimpleBooleanProperty(false);
}
