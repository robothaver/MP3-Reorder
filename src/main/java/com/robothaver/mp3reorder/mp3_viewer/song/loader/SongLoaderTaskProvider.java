package com.robothaver.mp3reorder.mp3_viewer.song.loader;

import com.robothaver.mp3reorder.mp3_viewer.song.task.domain.SongTask;
import com.robothaver.mp3reorder.mp3_viewer.song.task.SongTaskProvider;
import com.robothaver.mp3reorder.mp3_viewer.domain.Song;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class SongLoaderTaskProvider implements SongTaskProvider<Song> {
    private final String selectedPath;

    @Override
    public List<SongTask<Song>> getTasks() throws IOException {
        try (Stream<Path> files = Files.list(Paths.get(selectedPath))) {
            return files
                    .filter(file -> file.getFileName().toString().toLowerCase().endsWith(".mp3"))
                    .map(path -> new SongTask<>(new SongLoadTask(path), path))
                    .toList();
        }
    }
}
