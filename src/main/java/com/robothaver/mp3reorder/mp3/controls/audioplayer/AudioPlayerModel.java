package com.robothaver.mp3reorder.mp3.controls.audioplayer;

import javafx.beans.property.*;

public class AudioPlayerModel {
    private final StringProperty songName = new SimpleStringProperty();
    private final StringProperty artist = new SimpleStringProperty();
    private final ObjectProperty<byte[]> coverImageBytes = new SimpleObjectProperty<>();
    private final StringProperty songPathProperty = new SimpleStringProperty();

    private final DoubleProperty totalTimeSeconds = new SimpleDoubleProperty();
    private final DoubleProperty currentTimeSeconds = new SimpleDoubleProperty();
    private final StringProperty currentTimeText = new SimpleStringProperty("00:00");
    private final StringProperty totalTimeText = new SimpleStringProperty("00:00");

    private final BooleanProperty playing = new SimpleBooleanProperty(false);
    private final BooleanProperty muted = new SimpleBooleanProperty(false);
    private final DoubleProperty volume = new SimpleDoubleProperty(1.0);

    public String getSongName() {
        return songName.get();
    }

    public void setSongName(String songName) {
        this.songName.set(songName);
    }

    public StringProperty songNameProperty() {
        return songName;
    }

    public String getArtist() {
        return artist.get();
    }

    public void setArtist(String artist) {
        this.artist.set(artist);
    }

    public StringProperty artistProperty() {
        return artist;
    }

    public byte[] getCoverImageBytes() {
        return coverImageBytes.get();
    }

    public void setCoverImageBytes(byte[] coverImageBytes) {
        this.coverImageBytes.set(coverImageBytes);
    }

    public ObjectProperty<byte[]> coverImageBytesProperty() {
        return coverImageBytes;
    }

    public String getSongPathProperty() {
        return songPathProperty.get();
    }

    public void setSongPathProperty(String songPathProperty) {
        this.songPathProperty.set(songPathProperty);
    }

    public StringProperty songPathPropertyProperty() {
        return songPathProperty;
    }

    public double getTotalTimeSeconds() {
        return totalTimeSeconds.get();
    }

    public void setTotalTimeSeconds(double totalTimeSeconds) {
        this.totalTimeSeconds.set(totalTimeSeconds);
    }

    public DoubleProperty totalTimeSecondsProperty() {
        return totalTimeSeconds;
    }

    public double getCurrentTimeSeconds() {
        return currentTimeSeconds.get();
    }

    public void setCurrentTimeSeconds(double currentTimeSeconds) {
        this.currentTimeSeconds.set(currentTimeSeconds);
    }

    public DoubleProperty currentTimeSecondsProperty() {
        return currentTimeSeconds;
    }

    public String getCurrentTimeText() {
        return currentTimeText.get();
    }

    public void setCurrentTimeText(String currentTimeText) {
        this.currentTimeText.set(currentTimeText);
    }

    public StringProperty currentTimeTextProperty() {
        return currentTimeText;
    }

    public String getTotalTimeText() {
        return totalTimeText.get();
    }

    public void setTotalTimeText(String totalTimeText) {
        this.totalTimeText.set(totalTimeText);
    }

    public StringProperty totalTimeTextProperty() {
        return totalTimeText;
    }

    public boolean isPlaying() {
        return playing.get();
    }

    public void setPlaying(boolean playing) {
        this.playing.set(playing);
    }

    public BooleanProperty playingProperty() {
        return playing;
    }

    public boolean isMuted() {
        return muted.get();
    }

    public void setMuted(boolean muted) {
        this.muted.set(muted);
    }

    public BooleanProperty mutedProperty() {
        return muted;
    }

    public double getVolume() {
        return volume.get();
    }

    public void setVolume(double volume) {
        this.volume.set(volume);
    }

    public DoubleProperty volumeProperty() {
        return volume;
    }
}
