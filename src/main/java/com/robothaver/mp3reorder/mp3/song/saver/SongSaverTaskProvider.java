package com.robothaver.mp3reorder.mp3.song.saver;

import com.robothaver.mp3reorder.mp3.domain.Song;
import com.robothaver.mp3reorder.mp3.song.task.SongTaskProvider;
import com.robothaver.mp3reorder.mp3.song.task.domain.SongTask;

import java.nio.file.Path;
import java.util.List;

public class SongSaverTaskProvider implements SongTaskProvider<Void> {
    private final List<Song> songs;
    private final Path savePath;

    public SongSaverTaskProvider(List<Song> songs, Path savePath) {
        this.songs = songs;
        this.savePath = savePath;
    }

    public SongSaverTaskProvider(List<Song> songs) {
        this.songs = songs;
        this.savePath = null;
    }

    @Override
    public List<SongTask<Void>> getTasks() {
        if (savePath == null) {
            return songs
                    .stream()
                    .filter(song -> song.getFileChanged().get())
                    .map(song -> new SongTask<>(new SongSaveTask(song), song.getPath()))
                    .toList();
        }
        return songs
                .stream()
                .map(song -> new SongTask<>(new SongSaveAsTask(song, savePath), song.getPath()))
                .toList();
    }
}
