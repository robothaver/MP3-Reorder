package com.robothaver.mp3reordergradle.mp3viewver.song_loader;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SongLoadingProgress {
    private final IntegerProperty allSongs = new SimpleIntegerProperty();
    private final IntegerProperty songsLoaded = new SimpleIntegerProperty();

    public int getAllSongs() {
        return allSongs.get();
    }

    public IntegerProperty allSongsProperty() {
        return allSongs;
    }

    public int getSongsLoaded() {
        return songsLoaded.get();
    }

    public IntegerProperty songsLoadedProperty() {
        return songsLoaded;
    }
}
