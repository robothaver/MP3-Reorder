package com.robothaver.mp3reordergradle.mp3_viewer;

import com.robothaver.mp3reordergradle.mp3_viewer.song_loader.SongLoadingProgress;
import javafx.beans.property.*;
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

    public String getSelectedPath() {
        return selectedPath.get();
    }

    public StringProperty selectedPathProperty() {
        return selectedPath;
    }

    public int getSelectedSongIndex() {
        return selectedSongIndex.get();
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
