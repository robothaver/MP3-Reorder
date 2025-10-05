package com.robothaver.mp3reorder.dialog.progress;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Data;

@Data
public class ProgressState {
    private final IntegerProperty allTask = new SimpleIntegerProperty();
    private final IntegerProperty done = new SimpleIntegerProperty();
}
