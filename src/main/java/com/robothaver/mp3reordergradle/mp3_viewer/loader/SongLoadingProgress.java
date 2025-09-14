package com.robothaver.mp3reordergradle.mp3_viewer.loader;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

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
