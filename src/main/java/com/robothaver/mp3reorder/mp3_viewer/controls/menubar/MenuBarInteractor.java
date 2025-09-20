package com.robothaver.mp3reorder.mp3_viewer.controls.menubar;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MenuBarInteractor {
    private final MenuBarModel model;

    public void selectTheme(Themes theme) {
        model.getSelectedTheme().set(theme);
        Application.setUserAgentStylesheet(theme.getTheme().getUserAgentStylesheet());
    }

    public void setSize(Parent root, Size size) {
        model.getSelectedSize().set(size);
        root.setStyle("-fx-font-size: %dpx".formatted(size.getFontSize()));
    }
}
