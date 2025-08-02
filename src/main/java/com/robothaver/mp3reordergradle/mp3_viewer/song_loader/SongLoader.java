package com.robothaver.mp3reordergradle.mp3_viewer.song_loader;

import com.robothaver.mp3reordergradle.mp3_viewer.Song;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Dialog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class SongLoader extends Task<List<Song>> {
    private final String selectedPath;
    private final Dialog<Void> dialog;
    private final SongLoadingProgress songLoadingProgress;
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public SongLoader(String selectedPath, Dialog<Void> dialog, SongLoadingProgress songLoadingProgress) {
        this.selectedPath = selectedPath;
        this.dialog = dialog;
        this.songLoadingProgress = songLoadingProgress;
    }

    @Override
    protected List<Song> call() {
        System.out.println("Loading songs from " + selectedPath);

        Platform.runLater(() -> {
            dialog.show();
            songLoadingProgress.songsLoadedProperty().setValue(0);
        });

        List<Song> songs = new ArrayList<>();
        List<Future<Song>> futures = getTaskFutures();
        for (Future<Song> future : futures) {
            try {
                Song result = future.get();
                songs.add(result);
            } catch (ExecutionException | InterruptedException e) {
                System.err.println("Task failed: " + e.getMessage());
            }
        }

        songs.sort(Song::compareTo);
        assignTrackNumbers(songs);
        return songs.stream().sorted(Comparator.comparingInt(Song::getTrack)).toList();
    }

    private List<Future<Song>> getTaskFutures() {
        try (Stream<Path> stream = Files.list(Paths.get(selectedPath))) {
            List<SongTask> songs = stream
                    .filter(file -> file.getFileName().toString().endsWith(".mp3"))
                    .map(path -> new SongTask(path, songLoadingProgress))
                    .toList();
            Platform.runLater(() -> songLoadingProgress.allSongsProperty().setValue(songs.size()));

            return executor.invokeAll(songs);
        } catch (IOException e) {
            System.out.println("Selected path not found!");
            throw new IllegalStateException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
