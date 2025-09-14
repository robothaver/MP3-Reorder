package com.robothaver.mp3reordergradle.mp3_viewer.song.loader;

import com.robothaver.mp3reordergradle.mp3_viewer.song.domain.Song;
import javafx.application.Platform;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class SongLoader extends Task<List<Song>> {
    private final String selectedPath;
    private final SongLoadingProgress songLoadingProgress;

    @Override
    protected List<Song> call() {
        System.out.println("Loading songs from " + selectedPath);

        Platform.runLater(() -> songLoadingProgress.songsLoadedProperty().setValue(0));
        return loadSongs();
    }

    private List<Song> loadSongs() {
        try (Stream<Path> stream = Files.list(Paths.get(selectedPath))) {
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                List<SongTask> songTasks = stream
                        .filter(file -> file.getFileName().toString().endsWith(".mp3"))
                        .map(SongTask::new)
                        .toList();

                Platform.runLater(() -> songLoadingProgress.allSongsProperty().setValue(songTasks.size()));

                Stream<CompletableFuture<Song>> songFutures = songTasks.stream()
                        .map(songTask -> CompletableFuture.supplyAsync(() -> loadSong(songTask), executor));

                return songFutures
                        .map(this::getFutureResult)
                        .filter(Objects::nonNull)
                        .toList();
            }
        } catch (IOException e) {
            System.out.println("Selected path not found!");
            throw new IllegalStateException(e);
        }
    }

    private Song getFutureResult(CompletableFuture<Song> completableFuture) {
        try {
            return completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Failed to retrieve song: " + e);
            return null;
        }
    }

    private Song loadSong(SongTask songTask) {
        Song song = null;
        try {
            song = songTask.call();
        } catch (Exception e) {
            System.out.println("Failed to load song: " + e);
        }
        Platform.runLater(() -> songLoadingProgress.songsLoadedProperty().setValue(songLoadingProgress.getSongsLoaded() + 1));
        return song;
    }
}
