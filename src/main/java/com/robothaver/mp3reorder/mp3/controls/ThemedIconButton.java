package com.robothaver.mp3reorder.mp3.controls;

import javafx.scene.control.Button;
import lombok.Getter;

@Getter
public class ThemedIconButton extends Button {
    private final ThemedIconLabel iconLabel;

    public ThemedIconButton(String text, String icon) {
        super(text);

        iconLabel = new ThemedIconLabel(text, icon);

        setGraphic(iconLabel);
    }

    public ThemedIconButton(String text, String icon, double iconSize) {
        this(text, icon);
        iconLabel.setIconSize(iconSize, iconSize);
    }
}
