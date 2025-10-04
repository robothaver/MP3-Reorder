package com.robothaver.mp3reorder.mp3_viewer.controls.detailes.controls.genre;

import com.mpatric.mp3agic.ID3v1Genres;
import lombok.Data;

@Data
public class SongGenreOption {
    public static final String DEFAULT_NAME = "Not set";
    private final int genre;
    private final String genreDescription;

    @Override
    public String toString() {
        if (genreDescription != null && !genreDescription.equalsIgnoreCase("null") && !genreDescription.isBlank()) {
            if (genre != -1) {
                return genre + " " + genreDescription;
            } else {
                return genreDescription;
            }
        } else if (genre != -1) {
            return genre + " " + ID3v1Genres.GENRES[genre];
        } else {
            return DEFAULT_NAME;
        }
    }
}
