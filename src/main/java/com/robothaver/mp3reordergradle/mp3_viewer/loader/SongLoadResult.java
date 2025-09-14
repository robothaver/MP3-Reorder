package com.robothaver.mp3reordergradle.mp3_viewer.loader;

import com.robothaver.mp3reordergradle.mp3_viewer.Song;
import lombok.Data;

import java.util.List;

@Data
public class SongLoadResult {
    private final List<Song> songs;
    private final boolean usedExistingTracks;
}
