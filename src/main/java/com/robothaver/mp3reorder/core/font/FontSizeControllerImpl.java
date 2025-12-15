package com.robothaver.mp3reorder.core.font;

import javafx.scene.Parent;

public class FontSizeControllerImpl implements FontSizeController {
    private static FontSizeController instance = null;
    private static Parent parent;

    @Override
    public void setFontSize(Size size) {
        parent.setStyle("-fx-font-size: %dpx".formatted(size.getFontSize()));
    }

    public static void initialize(Parent parent) {
        if (instance != null) throw new IllegalStateException("FontSizeController is already initialized!");
        FontSizeControllerImpl.parent = parent;
        instance = new FontSizeControllerImpl();
    }

    public static FontSizeController getInstance() {
        if (instance == null) throw new IllegalStateException("FontSizeController has not been initialized!");
        return instance;
    }
}
