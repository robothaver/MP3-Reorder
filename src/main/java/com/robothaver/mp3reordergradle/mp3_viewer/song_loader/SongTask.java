package com.robothaver.mp3reordergradle.mp3_viewer.song_loader;

import com.mpatric.mp3agic.Mp3File;
import com.robothaver.mp3reordergradle.mp3_viewer.Song;
import javafx.application.Platform;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class SongTask implements Callable<Song> {
    private final Path file;
    private final SongLoadingProgress songLoadingProgress;

    @Override
    public Song call() throws Exception {
        Mp3File mp3File = new Mp3File(file);
        Song song = new Song(mp3File, file);
        int trackNumber = -1;
        if (mp3File.hasId3v2Tag()) {
            song.titleProperty().setValue(mp3File.getId3v2Tag().getTitle());
            trackNumber = getTrackFromTag(mp3File);
        }
        song.trackProperty().setValue(trackNumber);
        song.fileNameProperty().setValue(file.getFileName().toString());

        Platform.runLater(() -> songLoadingProgress.songsLoadedProperty().setValue(songLoadingProgress.getSongsLoaded() + 1));
        return song;
    }

    private int getTrackFromTag(Mp3File file) {
        String track = file.getId3v2Tag().getTrack();
        if (track == null) {
            return -1;
        } else {
            try {
                return Integer.parseInt(track);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
    }
}
