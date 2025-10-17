package com.robothaver.mp3reorder.mp3_viewer.song.loader;

import com.robothaver.mp3reorder.dialog.error.Error;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import lombok.Data;

import java.util.List;

@Data
public class SongLoaderResult {
    private final List<Song> songs;
    private final List<Error> errors;
}
