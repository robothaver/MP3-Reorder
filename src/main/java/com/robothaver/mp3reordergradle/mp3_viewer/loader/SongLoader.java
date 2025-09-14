package com.robothaver.mp3reordergradle.mp3_viewer.loader;

import com.robothaver.mp3reordergradle.mp3_viewer.Song;
import com.robothaver.mp3reordergradle.mp3_viewer.utils.MP3FileUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Dialog;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class SongLoader extends Task<SongLoadResult> {
    private final String selectedPath;
    private final Dialog<Void> dialog;
    private final SongLoadingProgress songLoadingProgress;
    private List<Song> tracks = new ArrayList<>();

    @Override
    protected SongLoadResult call() {
        System.out.println("Loading songs from " + selectedPath);

        Platform.runLater(() -> {
            dialog.show();
            songLoadingProgress.songsLoadedProperty().setValue(0);
        });

        List<Song> songs = loadSongs();
        boolean existingTracksAreValid = existingTracksAreValid(songs);
        songs = assignTrackNumbers(songs, existingTracksAreValid);

        return new SongLoadResult(songs, existingTracksAreValid);
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

    private boolean existingTracksAreValid(List<Song> songs) {
        tracks = new ArrayList<>();
        for (Song song : songs) {
            // Has track read from the file
            if (song.getTrack() != -1) {
                Song duplicateSong = tracks.stream().filter(song1 -> song1.getTrack() == song.getTrack()).findFirst().orElse(null);
                if (duplicateSong != null) {
                    return false;
                } else {
                    tracks.add(song);
                }
            }
        }
        return true;
    }

    private List<Song> assignTrackNumbers(List<Song> songs, boolean existingTracksAreValid) {
        if (existingTracksAreValid) {
            return assignTracksWithExisting(songs);
        } else {
            return assignNewTracks(songs);
        }
    }

    private List<Song> assignNewTracks(List<Song> songs) {
        List<Song> sorted = songs.stream().sorted((o1, o2) -> MP3FileUtils.compareFileNames(o2.getFileName(), o1.getFileName())).toList();
        for (int i = 0; i < sorted.size(); i++) {
            sorted.get(i).setTrack(i + 1);
        }
        return sorted;
    }

    private List<Song> assignTracksWithExisting(List<Song> songs) {
        List<Song> songsWithoutTracks = new ArrayList<>();
        List<Song> sortedSongs = new ArrayList<>();

        for (Song song : songs) {
            if (song.getTrack() == -1) {
                songsWithoutTracks.add(song);
            }
        }
        songsWithoutTracks.sort((o1, o2) -> MP3FileUtils.compareFileNames(o2.getFileName(), o1.getFileName()));

        for (int i = 0; i < songs.size(); i++) {
            // If there is a song with a track that is equal to the current index
            Song songWithTrack = getSongWithTrack(tracks, i + 1);
            if (songWithTrack != null) {
                sortedSongs.add(songWithTrack);
            } else {
                // Else add the next song without track and update its track
                Song songWithoutTrack = songsWithoutTracks.getFirst();
                songWithoutTrack.setTrack(i + 1);
                sortedSongs.add(songWithoutTrack);
                songsWithoutTracks.removeFirst();
            }
        }

        return sortedSongs;
    }

    private Song getSongWithTrack(List<Song> songs, int track) {
        return songs.stream().filter(song -> song.getTrack() == track).findFirst().orElse(null);
    }
}
