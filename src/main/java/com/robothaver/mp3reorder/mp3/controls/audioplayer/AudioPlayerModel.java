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
    private final BooleanProperty muted = new SimpleBooleanProperty(true);
    private final DoubleProperty volume = new SimpleDoubleProperty(0.0);

    private Runnable onPlayNext;
    private Runnable onPlayPrevious;
    private Runnable onTitleClicked;

    public String getSongName() {
        return songName.get();
    }

    public StringProperty songNameProperty() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName.set(songName);
    }

    public String getArtist() {
        return artist.get();
    }

    public StringProperty artistProperty() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist.set(artist);
    }

    public byte[] getCoverImageBytes() {
        return coverImageBytes.get();
    }

    public ObjectProperty<byte[]> coverImageBytesProperty() {
        return coverImageBytes;
    }

    public void setCoverImageBytes(byte[] coverImageBytes) {
        this.coverImageBytes.set(coverImageBytes);
    }

    public String getSongPathProperty() {
        return songPathProperty.get();
    }

    public StringProperty songPathPropertyProperty() {
        return songPathProperty;
    }

    public void setSongPathProperty(String songPathProperty) {
        this.songPathProperty.set(songPathProperty);
    }

    public double getTotalTimeSeconds() {
        return totalTimeSeconds.get();
    }

    public DoubleProperty totalTimeSecondsProperty() {
        return totalTimeSeconds;
    }

    public void setTotalTimeSeconds(double totalTimeSeconds) {
        this.totalTimeSeconds.set(totalTimeSeconds);
    }

    public double getCurrentTimeSeconds() {
        return currentTimeSeconds.get();
    }

    public DoubleProperty currentTimeSecondsProperty() {
        return currentTimeSeconds;
    }

    public void setCurrentTimeSeconds(double currentTimeSeconds) {
        this.currentTimeSeconds.set(currentTimeSeconds);
    }

    public String getCurrentTimeText() {
        return currentTimeText.get();
    }

    public StringProperty currentTimeTextProperty() {
        return currentTimeText;
    }

    public void setCurrentTimeText(String currentTimeText) {
        this.currentTimeText.set(currentTimeText);
    }

    public String getTotalTimeText() {
        return totalTimeText.get();
    }

    public StringProperty totalTimeTextProperty() {
        return totalTimeText;
    }

    public void setTotalTimeText(String totalTimeText) {
        this.totalTimeText.set(totalTimeText);
    }

    public boolean isPlaying() {
        return playing.get();
    }

    public BooleanProperty playingProperty() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing.set(playing);
    }

    public boolean isMuted() {
        return muted.get();
    }

    public BooleanProperty mutedProperty() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted.set(muted);
    }

    public double getVolume() {
        return volume.get();
    }

    public DoubleProperty volumeProperty() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume.set(volume);
    }

    public Runnable getOnPlayNext() {
        return onPlayNext;
    }

    public void setOnPlayNext(Runnable onPlayNext) {
        this.onPlayNext = onPlayNext;
    }

    public Runnable getOnPlayPrevious() {
        return onPlayPrevious;
    }

    public void setOnPlayPrevious(Runnable onPlayPrevious) {
        this.onPlayPrevious = onPlayPrevious;
    }

    public Runnable getOnTitleClicked() {
        return onTitleClicked;
    }

    public void setOnTitleClicked(Runnable onTitleClicked) {
        this.onTitleClicked = onTitleClicked;
    }
}
