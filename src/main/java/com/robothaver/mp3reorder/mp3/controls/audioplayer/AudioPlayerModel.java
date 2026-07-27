package com.robothaver.mp3reorder.mp3.controls.audioplayer;

import javafx.beans.property.*;
import lombok.Getter;

@Getter
public class AudioPlayerModel {
    private final StringProperty songName = new SimpleStringProperty();
    private final StringProperty artist = new SimpleStringProperty();
    private final ObjectProperty<byte[]> coverImageBytes = new SimpleObjectProperty<>();
    private final StringProperty songPathProperty = new SimpleStringProperty();

    private final DoubleProperty totalTime = new SimpleDoubleProperty();
    private final DoubleProperty currentTime = new SimpleDoubleProperty();
    private final StringProperty currentTimeText = new SimpleStringProperty("00:00");
    private final StringProperty totalTimeText = new SimpleStringProperty("00:00");

    private final BooleanProperty isPlaying = new SimpleBooleanProperty(false);
    private final BooleanProperty isMuted = new SimpleBooleanProperty(false);
    private final DoubleProperty volumeProperty = new SimpleDoubleProperty(1.0);
}
