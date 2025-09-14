package com.robothaver.mp3reordergradle.mp3_viewer;

import com.robothaver.mp3reordergradle.mp3_viewer.loader.SongLoadingProgress;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

@Getter
public class MP3Model {
    private final ObservableList<Song> songs = FXCollections.observableArrayList();
    private final StringProperty selectedPath = new SimpleStringProperty("SomeSet");
    private final IntegerProperty selectedSongIndex = new SimpleIntegerProperty();
    private final SongLoadingProgress songLoadingProgress = new SongLoadingProgress();

    public String getSelectedPath() {
        return selectedPath.get();
    }

    public StringProperty selectedPathProperty() {
        return selectedPath;
    }

    public IntegerProperty selectedSongIndexProperty() {
        return selectedSongIndex;
    }

    public int getSelectedSongIndex() {
        return selectedSongIndex.get();
    }
}
