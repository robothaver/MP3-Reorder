package com.robothaver.mp3reorder.mp3.controls.audioplayer;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import lombok.extern.log4j.Log4j2;

import java.nio.file.Path;

import static com.robothaver.mp3reorder.mp3.controls.audioplayer.Utils.formatDuration;

@Log4j2
public class AudioPlayerInteractor {
    private final AudioPlayerModel model;

    private MediaPlayer mediaPlayer;
    private double selectedVolume;
    private boolean endOfMedia;

    public AudioPlayerInteractor(AudioPlayerModel model) {
        this.model = model;
        model.playingProperty().addListener((_, _, _) ->
                onPlayingStateChanged());
    }

    public void playSong(String songName, String artis, byte[] coverImage, String path) {
        model.setSongName(songName);
        model.setArtist(artis);
        model.setCoverImageBytes(coverImage);

        if (mediaPlayer != null) mediaPlayer.dispose();

        Media media = new Media(Path.of(path).toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnReady(() -> {
            model.setPlaying(true);
            double totalLengthSeconds = media.getDuration().toSeconds();
            model.setTotalTimeSeconds(totalLengthSeconds);
            model.setTotalTimeText(totalLengthSeconds > 0 ? formatDuration(media.getDuration()) : "00:00");
            mediaPlayer.play();
        });
        mediaPlayer.setOnEndOfMedia(() -> {
            endOfMedia = true;
            model.setPlaying(false);
        });
        mediaPlayer.setOnError(() -> log.error(mediaPlayer.getError()));
        mediaPlayer.volumeProperty().bind(model.volumeProperty());
        mediaPlayer.currentTimeProperty().addListener(_ -> onCurrentPlayTimeChanged());
        endOfMedia = false;
    }

    public void onTogglePlay() {
        if (mediaPlayer == null) return;

        model.setPlaying(!model.isPlaying());
    }

    public void toggleMute() {
        boolean muted = !model.isMuted();
        model.setMuted(muted);
        if (muted) selectedVolume = model.getVolume();
        model.setVolume(muted ? 0 : selectedVolume);
    }

    public void onVolumeChanged() {
        boolean muted = model.isMuted();
        double volume = model.getVolume();
        if (muted && volume > 0) {
            model.setMuted(false);
        }
    }

    public void onSeek(double seconds) {
        if (mediaPlayer == null) return;
        endOfMedia = false;
        switch (mediaPlayer.getStatus()) {
            case PLAYING, PAUSED, STOPPED, READY -> mediaPlayer.seek(Duration.seconds(seconds));
            default -> {
            }
        }
    }

    private void onPlayingStateChanged() {
        if (mediaPlayer == null) return;
        MediaPlayer.Status status = mediaPlayer.getStatus();
        boolean readyOrPlaying = status == MediaPlayer.Status.READY ||
                status == MediaPlayer.Status.PLAYING ||
                status == MediaPlayer.Status.PAUSED ||
                status == MediaPlayer.Status.STOPPED;
        if (!readyOrPlaying) return;

        if (endOfMedia) onSeek(0);

        if (model.isPlaying()) {
            mediaPlayer.play();
        } else {
            mediaPlayer.pause();
        }
    }

    private void onCurrentPlayTimeChanged() {
        Duration duration = mediaPlayer.getCurrentTime();
        double seconds = duration != null && !duration.equals(Duration.ZERO) ? duration.toSeconds() : 0;

        model.setCurrentTimeText(seconds > 0 ? formatDuration(duration) : "00:00");
        model.setCurrentTimeSeconds(seconds);
    }
}
