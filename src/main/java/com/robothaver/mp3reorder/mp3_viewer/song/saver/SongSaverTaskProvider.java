package com.robothaver.mp3reorder.mp3_viewer.song.saver;

import com.robothaver.mp3reorder.mp3_viewer.song.task.domain.SongTask;
import com.robothaver.mp3reorder.mp3_viewer.song.task.SongTaskProvider;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
public class SongSaverTaskProvider implements SongTaskProvider<Void> {
    private final List<Song> songs;
    private final Path savePath = null;

    @Override
    public List<SongTask<Void>> getTasks() {
        return songs
                .stream()
                .filter(song -> song.getFileChanged().get())
                .map(song -> new SongTask<>(new SongSaveTask(song, savePath), song.getPath()))
                .toList();
    }
}
