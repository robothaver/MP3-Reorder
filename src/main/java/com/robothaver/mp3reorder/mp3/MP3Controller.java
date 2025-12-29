package com.robothaver.mp3reorder.mp3;

import com.robothaver.mp3reorder.core.BaseController;
import com.robothaver.mp3reorder.mp3.song.load.SongLoader;
import com.robothaver.mp3reorder.mp3.song.load.SongLoaderImpl;
import javafx.scene.layout.Region;

import java.nio.file.Path;


public class MP3Controller extends BaseController<Region> {
    private final SongLoader songLoader;
    private final MP3Model model;

    public MP3Controller() {
        model = new MP3Model();
        viewBuilder = new MP3ViewBuilder(model, this::onLoadSongs);
        songLoader = new SongLoaderImpl(model, viewBuilder);
    }

    public void loadSongs(Path selectedDir) {
        model.selectedPathProperty().set(selectedDir.toString());
        songLoader.loadSongs();
    }

    private void onLoadSongs() {
        songLoader.loadSongs();
    }
}
