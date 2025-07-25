package com.robothaver.mp3reordergradle.mp3viewver;

import javafx.collections.ObservableList;
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
                interactor::setTracksForSongsByName
        );
    }

    private void loadSongs() {
        System.out.println("BUTTON CLIUCKED");
        SongLoader songLoader = new SongLoader(model.selectedPathProperty().getValue());
        songLoader.setOnSucceeded(event -> {
            System.out.println("LOADED ALL SONGS");
            ObservableList<Song> songs = model.getFiles();
            songs.clear();
            songs.addAll(songLoader.getValue());
            System.out.println(songLoader.getValue());
        });
        Thread thread = new Thread(songLoader);
        thread.setDaemon(true);
        thread.start();
    }

    public Region getView() {
        return viewBuilder.build();
    }
}
