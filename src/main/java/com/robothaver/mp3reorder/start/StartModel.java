package com.robothaver.mp3reorder.start;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Data;

@Data
public class StartModel {
    private final BooleanProperty showStart = new SimpleBooleanProperty(true);
}
