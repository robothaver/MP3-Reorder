package com.robothaver.mp3reordergradle.mp3viewver;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MP3Model {
    private final ObservableList<Song> files = FXCollections.observableArrayList();
    private final StringProperty selectedPath = new SimpleStringProperty("bulira  zenék");
    private final IntegerProperty selectedSongIndex = new SimpleIntegerProperty();
//    private final StringProperty selectedPath = new SimpleStringProperty("sorted");

    public ObservableList<Song> getFiles() {
        return files;
    }

    public StringProperty selectedPathProperty() {
        return selectedPath;
    }

    public IntegerProperty selectedSongIndexProperty() {
        return selectedSongIndex;
    }
}
