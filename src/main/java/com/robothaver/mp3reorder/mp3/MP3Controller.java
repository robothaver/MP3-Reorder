package com.robothaver.mp3reorder.mp3;

import com.robothaver.mp3reorder.core.BaseController;
import com.robothaver.mp3reorder.mp3.song.loader.SongLoader;
import com.robothaver.mp3reorder.mp3.song.loader.SongLoaderImpl;
import javafx.scene.layout.Region;

public class MP3Controller extends BaseController<Region> {
    private final SongLoader songLoader;

    public MP3Controller() {
        MP3Model model = new MP3Model();
        viewBuilder = new MP3ViewBuilder(model, this::onLoadSongs);
        songLoader = new SongLoaderImpl(model, viewBuilder);
    }

    private void onLoadSongs() {
        songLoader.loadSongs();
    }

    @Override
    public Region getView() {
        Region build = viewBuilder.build();
        songLoader.loadSongs();
        return build;
    }
}
