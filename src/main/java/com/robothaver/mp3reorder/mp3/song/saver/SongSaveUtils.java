package com.robothaver.mp3reorder.mp3.song.saver;

import com.robothaver.mp3reorder.mp3.domain.Song;

public class SongSaveUtils {

    private SongSaveUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String buildSongName(Song song) {
        String newSongName = song.getFileName().trim();
        if (!newSongName.toLowerCase().endsWith(".mp3")) {
            newSongName = newSongName + ".mp3";
        }
        return newSongName;
    }
}
