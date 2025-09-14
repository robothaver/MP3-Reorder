package com.robothaver.mp3reorder.mp3_viewer.song.track.editor;

public enum TrackConflictSolutions {
    INSERT, PUT, CANCEL;

    public String getDisplayName() {
        String lowerCase = name().toLowerCase();
        return lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
    }

    public static TrackConflictSolutions fromDisplayName(String displayName) {
        return switch (displayName) {
            case "Insert" -> INSERT;
            case "Put" -> PUT;
            case "Cancel" -> CANCEL;
            default -> throw new IllegalArgumentException("Unexpected value: " + displayName);
        };
    }
}
