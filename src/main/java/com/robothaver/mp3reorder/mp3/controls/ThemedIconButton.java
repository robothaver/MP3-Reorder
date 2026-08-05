package com.robothaver.mp3reorder.mp3.controls;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import lombok.Getter;

@Getter
public class ThemedIconButton extends Button {
    private final ThemedIconLabel iconLabel;

    public ThemedIconButton(String text, String icon) {
        super(text);
        iconLabel = new ThemedIconLabel(text, icon);

        setGraphic(iconLabel);
    }

    public ThemedIconButton(String text, Image icon) {
        super(text);
        iconLabel = new ThemedIconLabel(text, icon);

        setGraphic(iconLabel);
    }

    public ThemedIconButton(String text, String icon, double iconSize) {
        this(text, icon);
        iconLabel.setIconSize(iconSize, iconSize);
    }

    public ThemedIconButton(String text, Image icon, double iconSize) {
        this(text, icon);
        iconLabel.setIconSize(iconSize, iconSize);
    }

    public ThemedIconButton(String icon, double iconSize) {
        this("", icon, iconSize);
    }

    public ThemedIconButton(Image icon, double iconSize) {
        this("", icon, iconSize);
    }
}
