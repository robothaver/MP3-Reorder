package com.robothaver.mp3reordergradle.mp3_viewer;

import com.robothaver.mp3reordergradle.mp3_viewer.controls.SongLoadingDialogViewBuilder;
import com.robothaver.mp3reordergradle.mp3_viewer.song_loader.SongLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class MP3Controller {
    private final MP3Model model;
    private final Builder<Region> viewBuilder;
    private final MP3Interactor interactor;

    public MP3Controller() {
        this.model = new MP3Model();
        this.interactor = new MP3Interactor(model);
        this.viewBuilder = new MP3ViewBuilder(
                model,
                actionEvent -> loadSongs(),
                interactor::moveSelectedSongUp,
                interactor::moveSelectedSongDown,
                interactor::onTrackChangedForSong,
                interactor::setTracksForSongsByName
        );
    }

    private void loadSongs() {
        SongLoadingDialogViewBuilder dialogViewBuilder = new SongLoadingDialogViewBuilder(model.getSongLoadingProgress(), model.getSelectedPath());
        Dialog<Void> dialog = dialogViewBuilder.build();

        SongLoader songLoader = new SongLoader(model.getSelectedPath(), dialog, model.getSongLoadingProgress());
        songLoader.setOnSucceeded(event -> {
            System.out.println("Success");
            model.getSongs().setAll(songLoader.getValue());
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getScene().getWindow().hide();
        });
        songLoader.setOnFailed(event -> {
            Throwable ex = songLoader.getException();
            System.err.println("SongLoader failed: " + ex);
            ex.printStackTrace();
        });

        new Thread(songLoader).start();
    }

    public Region getView() {
        return viewBuilder.build();
    }
}
