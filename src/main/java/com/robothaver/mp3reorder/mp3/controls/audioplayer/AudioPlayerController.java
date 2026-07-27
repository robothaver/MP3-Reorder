package com.robothaver.mp3reorder.mp3.controls.audioplayer;

import com.robothaver.mp3reorder.core.BaseController;
import javafx.scene.layout.HBox;

public class AudioPlayerController extends BaseController<HBox> {

    private final AudioPlayerInteractor interactor;

    public AudioPlayerController() {
        AudioPlayerModel model = new AudioPlayerModel();
        interactor = new AudioPlayerInteractor(model);
        viewBuilder = new AudioPlayerViewBuilder(model, interactor::onTogglePlay, interactor::toggleMute, interactor::onVolumeChanged, interactor::onSeek);
    }

    public void playSong(String title, String artis, byte[] coverImage, String path) {
        interactor.playSong(title, artis, coverImage, path);
    }
}
