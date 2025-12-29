package com.robothaver.mp3reorder.mp3;

import com.robothaver.mp3reorder.mp3.controls.menubar.MenuBarModel;
import com.robothaver.mp3reorder.mp3.domain.Song;
import com.robothaver.mp3reorder.mp3.controls.search.SearchTextFieldModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

@Getter
public class MP3Model {
    private final ObservableList<Song> songs = FXCollections.observableArrayList();
    private final StringProperty selectedPath = new SimpleStringProperty();
    private final IntegerProperty selectedSongIndex = new SimpleIntegerProperty(-1);
    private final SearchTextFieldModel songSearch = new SearchTextFieldModel();
    private final MenuBarModel menuBarModel = new MenuBarModel();

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
