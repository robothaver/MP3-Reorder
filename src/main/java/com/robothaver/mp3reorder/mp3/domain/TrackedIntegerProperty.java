package com.robothaver.mp3reorder.mp3.domain;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TrackedIntegerProperty extends SimpleIntegerProperty {
    private BooleanProperty changed;

    @Override
    public void setValue(Number v) {
        super.setValue(v);
        changed.set(true);
    }
}
