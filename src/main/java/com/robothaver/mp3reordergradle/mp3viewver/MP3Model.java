package com.robothaver.mp3reordergradle.mp3viewver;

import com.robothaver.mp3reordergradle.mp3viewver.song_loader.SongLoadingProgress;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MP3Model {
    private final ObservableList<Song> songs = FXCollections.observableArrayList();
    private final StringProperty selectedPath = new SimpleStringProperty("bulira  zenék");
    private final IntegerProperty selectedSongIndex = new SimpleIntegerProperty();
    private SongLoadingProgress songLoadingProgress = new SongLoadingProgress();

    public ObservableList<Song> getSongs() {
        return songs;
    }

    public StringProperty selectedPathProperty() {
        return selectedPath;
    }

    public IntegerProperty selectedSongIndexProperty() {
        return selectedSongIndex;
    }

    public SongLoadingProgress getSongLoadingProgress() {
        return songLoadingProgress;
    }

    public void setSongLoadingProgress(SongLoadingProgress songLoadingProgress) {
        this.songLoadingProgress = songLoadingProgress;
    }
}
