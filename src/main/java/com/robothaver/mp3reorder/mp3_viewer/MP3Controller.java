package com.robothaver.mp3reorder.mp3_viewer;

import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.dialog.progress.ProgressDialogState;
import com.robothaver.mp3reorder.dialog.progress.ProgressState;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import com.robothaver.mp3reorder.mp3_viewer.song.loader.SongLoader;
import com.robothaver.mp3reorder.mp3_viewer.song.track.assigner.TrackAssigner;
import com.robothaver.mp3reorder.mp3_viewer.song.track.assigner.TrackAssignerImpl;
import com.robothaver.mp3reorder.mp3_viewer.song.track.assigner.TrackAssignerResult;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.List;

public class MP3Controller {
    private final MP3Model model;
    private final Builder<Region> viewBuilder;
    private final MP3Interactor interactor;

    public MP3Controller() {
        this.model = new MP3Model();
        this.interactor = new MP3Interactor(model);
        this.viewBuilder = new MP3ViewBuilder(
                model,
                this::loadSongs,
                interactor::moveSelectedSongUp,
                interactor::moveSelectedSongDown,
                interactor::onTrackChangedForSong,
                interactor::onSearchQueryChanged
        );
    }

    private void loadSongs() {
        ProgressState progressState = new ProgressState();
        ProgressDialogState dialogState = new ProgressDialogState(
                "Loading songs",
                "Songs loaded: ",
                "Loading songs...",
                progressState
        );

        SongLoader songLoader = new SongLoader(model.getSelectedPath(), progressState);
        songLoader.setOnSucceeded(event -> {
            System.out.println("Loaded songs successfully!");

            List<Song> songs = songLoader.getValue();
            TrackAssigner trackAssigner = new TrackAssignerImpl(songs);
            TrackAssignerResult trackAssignerResult = trackAssigner.assignTracks();
            model.getSongs().setAll(trackAssignerResult.getSongs());
            dialogState.getVisible().set(false);
        });
        songLoader.setOnFailed(event -> {
            Throwable ex = songLoader.getException();
            System.err.println("SongLoader failed: " + ex);
            ex.printStackTrace();
            dialogState.getVisible().set(false);
        });

        new Thread(songLoader).start();
        DialogManagerImpl.getInstance().showProgressDialog(dialogState);
    }

    public Region getView() {
        Region build = viewBuilder.build();
        loadSongs();
        return build;
    }
}
