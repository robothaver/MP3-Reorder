package com.robothaver.mp3reorder.mp3.controls.audioplayer;

import javafx.util.Duration;

public class Utils {

    private Utils() {
        /* This utility class should not be instantiated */
    }

    public static String formatDuration(Duration duration) {
        long seconds = (long) duration.toSeconds();
        return seconds < 3600
                ? String.format("%02d:%02d", (seconds % 3600) / 60, seconds % 60)
                : String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
    }
}
