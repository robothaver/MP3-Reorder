package com.robothaver.mp3reorder.mp3_viewer.utils;

public class Utils {
    public static final String TRACK_CONFLICT_MESSAGE = """
                            This track is already assigned to a different song.
                            
                            - Insert: Move the current song to the selected track and shift the existing songs at that track.
                            - Put: Assign the track to the current song, ignoring the existing conflict
                            
                            What would you like to do?""";

    private Utils() {
        throw new IllegalStateException("Utility class");
    }
}
