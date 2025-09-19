package com.robothaver.mp3reorder.mp3_viewer.song.domain;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

@Getter
public class SongSearch {
    private final StringProperty searchQuery = new SimpleStringProperty("");
    private final BooleanProperty found = new SimpleBooleanProperty(true);
}
