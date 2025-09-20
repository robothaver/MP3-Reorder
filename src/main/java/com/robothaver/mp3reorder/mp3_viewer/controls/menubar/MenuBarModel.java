package com.robothaver.mp3reorder.mp3_viewer.controls.menubar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

@Getter
public class MenuBarModel {
    private final ObjectProperty<Themes> selectedTheme = new SimpleObjectProperty<>(Themes.PRIMER_DARK);
}
