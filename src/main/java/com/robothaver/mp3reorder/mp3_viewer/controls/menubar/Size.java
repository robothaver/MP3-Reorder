package com.robothaver.mp3reorder.mp3_viewer.controls.menubar;

public enum Size {
    AUTO(14, "Auto"),
    SMALL(11, "Small"),
    NORMAL(15, "Normal"),
    LARGE(18, "Large");

    private int fontSize;
    private String displayName;

    Size(int fontSize, String displayName) {
        this.fontSize = fontSize;
        this.displayName = displayName;
    }

    public int getFontSize() {
        return fontSize;
    }

    public String getDisplayName() {
        return displayName;
    }
}
