package com.robothaver.mp3reorder.mp3.controls.audioplayer;

import javafx.beans.property.*;
import lombok.Getter;

@Getter
public class AudioPlayerModel {
    private final StringProperty songName = new SimpleStringProperty();
    private final StringProperty artist = new SimpleStringProperty();
    private final ObjectProperty<byte[]> coverImageBytes = new SimpleObjectProperty<>();
    private final BooleanProperty isPlaying = new SimpleBooleanProperty();
    private final StringProperty songPathProperty = new SimpleStringProperty();
}
