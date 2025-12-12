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
}
