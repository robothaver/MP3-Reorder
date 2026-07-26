package com.robothaver.mp3reorder.core.utils;

import javafx.scene.image.Image;

import java.util.Objects;

public class ResourceHelper {

    private ResourceHelper() {
        /* This utility class should not be instantiated */
    }

    public static Image loadImage(String iconName) {
        return new Image(Objects.requireNonNull(ResourceHelper.class.getResourceAsStream("/icons/" + iconName)));
    }
}