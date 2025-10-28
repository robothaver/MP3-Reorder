package com.robothaver.mp3reorder.mp3_viewer.song.domain;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TrackedStringProperty extends SimpleStringProperty {
    private BooleanProperty changed;

    @Override
    public void set(String newValue) {
        super.set(newValue);
    }

    @Override
    public void setValue(String value) {
        super.set(value);
        changed.set(true);
    }
}
