package com.robothaver.mp3reorder.mp3.controls.audioplayer;

import com.robothaver.mp3reorder.core.BaseController;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Files;
import java.nio.file.Path;

public class AudioPlayerController extends BaseController<HBox> {
    public AudioPlayerController() {
        AudioPlayerModel model = new AudioPlayerModel();
        viewBuilder = new AudioPlayerViewBuilder(model, () -> {
            String path = "/Users/robothaver/Documents/DevProjects/Java/MP3 Reorder/bulira  zenék/1 Ricchi E Poveri - Sara Perche Ti Amo (Testo  Lyrics).mp3";
            Media media = new Media(Path.of(path).toUri().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(0.5);
            mediaPlayer.setOnReady(() -> {
                System.out.println("Media Player ready");
                mediaPlayer.play();
            });
        });
    }
}
