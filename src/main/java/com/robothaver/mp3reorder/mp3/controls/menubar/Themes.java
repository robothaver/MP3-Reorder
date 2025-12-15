package com.robothaver.mp3reorder.mp3.controls.menubar;

import atlantafx.base.theme.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Themes {
    CUPERTINO_DARK("Cupertino Dark", new CupertinoDark()),
    CUPERTINO_LIGHT("Cupertino Ligth", new CupertinoLight()),
    NORD_DARK("Nord Dark", new NordDark()),
    NORD_LIGHT("Nord Light", new NordLight()),
    PRIMER_DARK("Primer Dark", new PrimerDark()),
    PRIMER_LIGHT("Primer Light", new PrimerLight()),
    DRACULA("Dracula", new Dracula());

    private final String displayName;
    private final Theme theme;

    public static Themes getByName(String name) {
        return switch (name) {
            case "CUPERTINO_DARK" -> CUPERTINO_DARK;
            case "CUPERTINO_LIGHT" -> CUPERTINO_LIGHT;
            case "NORD_DARK" -> NORD_DARK;
            case "NORD_LIGHT" -> NORD_LIGHT;
            case "PRIMER_DARK" -> PRIMER_DARK;
            case "PRIMER_LIGHT" -> PRIMER_LIGHT;
            case "DRACULA" -> DRACULA;
            default -> throw new IllegalStateException("Unexpected value: " + name);
        };
    }
}
