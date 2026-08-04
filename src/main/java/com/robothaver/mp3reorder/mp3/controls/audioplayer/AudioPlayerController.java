package com.robothaver.mp3reorder.mp3.controls.audioplayer;

import com.robothaver.mp3reorder.core.BaseController;
import javafx.scene.layout.StackPane;
import lombok.Getter;

public class AudioPlayerController extends BaseController<StackPane> {

    private final AudioPlayerInteractor interactor;
    @Getter
    private final AudioPlayerModel model;

    public AudioPlayerController() {
        model = new AudioPlayerModel();
        interactor = new AudioPlayerInteractor(model);
        viewBuilder = new AudioPlayerViewBuilder(model, interactor::onTogglePlay, interactor::toggleMute, interactor::onVolumeChanged, interactor::onSeek);
    }

    public void playSong(String title, String artis, byte[] coverImage, String path) {
        interactor.playSong(title, artis, coverImage, path);
    }
}
