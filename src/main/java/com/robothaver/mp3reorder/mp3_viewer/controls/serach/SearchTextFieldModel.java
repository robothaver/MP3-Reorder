package com.robothaver.mp3reorder.mp3_viewer.controls.serach;

import com.robothaver.mp3reorder.mp3_viewer.domain.Song;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

@Getter
public class SearchTextFieldModel {
    private final StringProperty searchQuery = new SimpleStringProperty("");
    private final ObservableList<Song> results = FXCollections.observableArrayList();
    private final IntegerProperty selectedResultIndex = new SimpleIntegerProperty();
}
