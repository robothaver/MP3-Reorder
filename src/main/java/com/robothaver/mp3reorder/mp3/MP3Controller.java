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
        MP3Interactor interactor = new MP3Interactor(model);
        viewBuilder = new MP3ViewBuilder(
                model,
                this::onLoadSongs,
                interactor::closeDetailsSideMenu,
                interactor::onPlayNext,
                interactor::onPlayPrevious,
                interactor::onScrollToPlaying,
                interactor::onPlayPressedWhenEmpty,
                interactor::onAudioPlayerError,
                interactor::togglePlay
        );
        songLoader = new SongLoaderImpl(model);
    }

    public void loadSongs(Path selectedDir) {
        model.selectedPathProperty().set(selectedDir.toString());
        onLoadSongs();
    }

    private void onLoadSongs() {
        model.setSongInPlayer(null);
        model.getSongSearch().clear();
        songLoader.loadSongs();
    }
}
