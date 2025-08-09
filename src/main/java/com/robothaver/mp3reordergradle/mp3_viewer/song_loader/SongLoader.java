package com.robothaver.mp3reordergradle.mp3_viewer.song_loader;

import com.robothaver.mp3reordergradle.mp3_viewer.Song;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Dialog;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
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
    private final Dialog<Void> dialog;
    private final SongLoadingProgress songLoadingProgress;

    @Override
    protected List<Song> call() {
        System.out.println("Loading songs from " + selectedPath);

        Platform.runLater(() -> {
            dialog.show();
            songLoadingProgress.songsLoadedProperty().setValue(0);
        });

        List<Song> songs = loadSongs();
        assignTrackNumbers(songs);
        return songs.stream().sorted(Comparator.comparingInt(Song::getTrack)).toList();
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
                        .map(songTask -> CompletableFuture.supplyAsync(() -> {
                            Song song = null;
                            try {
                                song = songTask.call();
                            } catch (Exception e) {
                                System.out.println("Failed to load song: " + e);
                            }
                            Platform.runLater(() -> songLoadingProgress.songsLoadedProperty().setValue(songLoadingProgress.getSongsLoaded() + 1));
                            return song;
                        }, executor));


                return songFutures
                        .map(songCompletableFuture -> {
                            try {
                                return songCompletableFuture.get();
                            } catch (InterruptedException | ExecutionException e) {
                                System.out.println("Failed to retrieve song: " + e);
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .sorted()
                        .toList();

            }
        } catch (IOException e) {
            System.out.println("Selected path not found!");
            throw new IllegalStateException(e);
        }
    }

    private void assignTrackNumbers(List<Song> songs) {
        for (int i = 0; i < songs.size(); i++) {
            Song song = songs.get(i);
            if (song.getTrack() == -1) {
                song.setTrack(i + 1);
            }
        }
    }
}
