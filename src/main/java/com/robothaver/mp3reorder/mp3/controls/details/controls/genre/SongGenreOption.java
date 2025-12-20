package com.robothaver.mp3reorder.mp3.controls.details.controls.genre;

import lombok.Data;

@Data
public class SongGenreOption {
    private final int genre;
    private final String genreDescription;

    @Override
    public String toString() {
        return genreDescription;
    }
}
