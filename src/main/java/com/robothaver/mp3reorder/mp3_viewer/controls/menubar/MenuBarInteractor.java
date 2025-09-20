package com.robothaver.mp3reorder.mp3_viewer.controls.menubar;

import javafx.application.Application;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MenuBarInteractor {
    private final MenuBarModel model;

    public void selectTheme(Themes theme) {
        model.getSelectedTheme().set(theme);
        Application.setUserAgentStylesheet(theme.getTheme().getUserAgentStylesheet());
    }
}
