package com.robothaver.mp3reorder.core.font;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Size {
    AUTO(14),
    SMALL(11),
    NORMAL(15),
    LARGE(18);

    private final int fontSize;

    public static Size fromString(String sizeString) {
        return switch (sizeString) {
            case "AUTO" -> AUTO;
            case "SMALL" -> SMALL;
            case "NORMAL" -> NORMAL;
            case "LARGE" -> LARGE;
            default -> throw new IllegalStateException("Unexpected value: " + sizeString);
        };
    }
}
