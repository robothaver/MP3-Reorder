package com.robothaver.mp3reordergradle.mp3_viewer.controls;

import com.robothaver.mp3reordergradle.mp3_viewer.song_loader.SongLoadingProgress;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SongLoadingDialogViewBuilder implements Builder<Dialog<Void>> {
    private final SongLoadingProgress songLoadingProgress;
    private final String loadingSource;

    @Override
    public Dialog<Void> build() {
        VBox alertBody = new VBox();
        Label label = new Label("Loading songs from: " + loadingSource);
        Label loadingSongLabel = new Label();
        ProgressBar progressBar = new ProgressBar();

        songLoadingProgress.allSongsProperty().addListener(observable -> {
            loadingSongLabel.setText("Loading song %d/%d".formatted(songLoadingProgress.getSongsLoaded(), songLoadingProgress.getAllSongs()));
            progressBar.setProgress(0);
        });

        songLoadingProgress.songsLoadedProperty().addListener(observable -> {
            int allSongs = songLoadingProgress.getAllSongs();
            int songsLoaded = songLoadingProgress.getSongsLoaded();
            loadingSongLabel.setText("Loading song %d/%d".formatted(songsLoaded, allSongs));
            progressBar.setProgress((double) songsLoaded / allSongs);
        });

        progressBar.setMaxWidth(Double.MAX_VALUE);
        alertBody.setSpacing(20);
        alertBody.getChildren().addAll(label, loadingSongLabel, progressBar);

        Dialog<Void> dialog = new Dialog<>();
        dialog.setResizable(true);
        dialog.setWidth(400);
        dialog.getDialogPane().setContent(alertBody);

        return dialog;
    }
}
