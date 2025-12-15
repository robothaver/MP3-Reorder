package com.robothaver.mp3reorder.mp3.controls.menubar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Size {
    AUTO(14, "Auto"),
    SMALL(11, "Small"),
    NORMAL(15, "Normal"),
    LARGE(18, "Large");

    private final int fontSize;
    private final String displayName;

    public static Size getByName(String name) {
        return switch (name) {
            case "AUTO" -> AUTO;
            case "SMALL" -> SMALL;
            case "NORMAL" -> NORMAL;
            case "LARGE" -> LARGE;
            default -> throw new IllegalStateException("Unexpected value: " + name);
        };
    }
}
