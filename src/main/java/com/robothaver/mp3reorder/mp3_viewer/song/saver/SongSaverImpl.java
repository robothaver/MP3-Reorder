package com.robothaver.mp3reorder.mp3_viewer.song.saver;

import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class SongSaverImpl implements SongSaver {
    @Override
    public void SaveSongs(List<Song> songs) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<SongSaveTask> songTasks = songs
                    .stream()
                    .map(song -> new SongSaveTask(song, null))
                    .toList();

//            Platform.runLater(() -> songLoadingProgress.allSongsProperty().setValue(songTasks.size()));

            Stream<CompletableFuture<SaveResult>> songFutures = songTasks.stream()
                    .map(songTask -> CompletableFuture.supplyAsync(songTask::call, executor));

            List<SaveResult> list = songFutures
                    .map(this::getFutureResult)
                    .filter(Objects::nonNull)
                    .toList();
            System.out.println(list);
        }
    }

    @Override
    public void SaveAsSongs(List<Song> songs, Path savePath) {

    }

    private SaveResult getFutureResult(CompletableFuture<SaveResult> completableFuture) {
        try {
            return completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Failed to retrieve song: " + e);
            return null;
        }
    }
}
