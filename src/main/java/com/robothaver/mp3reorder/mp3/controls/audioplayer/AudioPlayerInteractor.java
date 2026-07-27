package com.robothaver.mp3reorder.mp3.controls.audioplayer;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.nio.file.Path;

import static com.robothaver.mp3reorder.mp3.controls.audioplayer.Utils.formatDuration;

@Log4j2
@RequiredArgsConstructor
public class AudioPlayerInteractor {
    private final AudioPlayerModel model;

    private MediaPlayer mediaPlayer;
    private double selectedVolume;

    public void playSong(String songName, String artis, byte[] coverImage, String path) {
        model.getSongName().set(songName);
        model.getArtist().set(artis);
        model.getCoverImageBytes().set(coverImage);

        Media media = new Media(Path.of(path).toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnReady(() -> {
            double totalLengthSeconds = media.getDuration().toSeconds();
            model.getIsPlaying().set(true);
            model.getTotalTime().set(totalLengthSeconds);
            model.getTotalTimeText().set(totalLengthSeconds > 0 ? formatDuration(media.getDuration()) : "00:00");
            mediaPlayer.play();
        });
        mediaPlayer.setOnError(() -> log.error(mediaPlayer.getError()));
        mediaPlayer.volumeProperty().bind(model.getVolumeProperty());
        mediaPlayer.currentTimeProperty().addListener(_ -> onCurrentPlayTimeChanged());
    }

    public void onTogglePlay() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            model.getIsPlaying().set(false);
        } else if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            mediaPlayer.play();
            model.getIsPlaying().set(true);
        }
    }

    public void toggleMute() {
        boolean muted = !model.getIsMuted().get();
        model.getIsMuted().set(muted);
        if (muted) selectedVolume = model.getVolumeProperty().get();
        model.getVolumeProperty().set(muted ? 0 : selectedVolume);
    }

    public void onVolumeChanged() {
        boolean muted = model.getIsMuted().get();
        double volume = model.getVolumeProperty().get();
        if (muted && volume > 0) {
            model.getIsMuted().set(false);
        }
    }

    public void onSeek(double seconds) {
        if (mediaPlayer == null) return;
        switch (mediaPlayer.getStatus()) {
            case PLAYING, PAUSED, READY -> mediaPlayer.seek(Duration.seconds(seconds));
            default -> {}
        }
    }

    private void onCurrentPlayTimeChanged() {
        Duration duration = mediaPlayer.getCurrentTime();
        double seconds = duration != null && !duration.equals(Duration.ZERO) ? duration.toSeconds() : 0;

        model.getCurrentTimeText().set(seconds > 0 ? formatDuration(duration) : "00:00");
        model.getCurrentTime().set(seconds);
    }
}
