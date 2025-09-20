package com.robothaver.mp3reorder.mp3_viewer.controls.menubar;

import atlantafx.base.theme.*;

public enum Themes {
    CUPERTINO_DARK("Cupertino Dark", new CupertinoDark()),
    CUPERTINO_LIGHT("Cupertino Ligth", new CupertinoLight()),
    NORD_DARK("Nord Dark", new NordDark()),
    NORD_LIGHT("Nord Light", new NordLight()),
    PRIMER_DARK("Primer Dark", new PrimerDark()),
    PRIMER_LIGHT("Primer Light", new PrimerLight()),
    DRACULA("Dracula", new Dracula());

    private String displayName;
    private Theme theme;

    Themes(String displayName, Theme theme) {
        this.displayName = displayName;
        this.theme = theme;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Theme getTheme() {
        return theme;
    }
}
