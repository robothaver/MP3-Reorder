package com.robothaver.mp3reorder.mp3_viewer.song.saver;

import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;

import java.nio.file.Path;
import java.util.List;

public interface SongSaver {
    void SaveSongs(List<Song> songs);
    void SaveAsSongs(List<Song> songs, Path savePath);
}
