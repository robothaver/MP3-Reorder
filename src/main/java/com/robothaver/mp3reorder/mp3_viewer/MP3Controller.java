package com.robothaver.mp3reorder.mp3_viewer;

import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.dialog.error.ErrorListAlertMessage;
import com.robothaver.mp3reorder.dialog.progress.ProgressDialogState;
import com.robothaver.mp3reorder.dialog.progress.ProgressState;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import com.robothaver.mp3reorder.mp3_viewer.song.loader.SongLoaderTaskProvider;
import com.robothaver.mp3reorder.mp3_viewer.song.task.SongTaskExecutor;
import com.robothaver.mp3reorder.mp3_viewer.song.task.domain.ProcessorResult;
import com.robothaver.mp3reorder.mp3_viewer.song.track.assigner.TrackAssigner;
import com.robothaver.mp3reorder.mp3_viewer.song.track.assigner.TrackAssignerImpl;
import com.robothaver.mp3reorder.mp3_viewer.song.track.assigner.TrackAssignerResult;
import com.robothaver.mp3reorder.mp3_viewer.song.track.assigner.TrackIssue;
import javafx.scene.control.Alert;
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

        SongTaskExecutor<Song> songProcessor = new SongTaskExecutor<>(new SongLoaderTaskProvider(model.getSelectedPath()), progressState);
        songProcessor.setOnSucceeded(event -> {
            ProcessorResult<Song> result = songProcessor.getValue();
            TrackAssigner trackAssigner = new TrackAssignerImpl(result.getResults());
            TrackAssignerResult trackAssignerResult = trackAssigner.assignTracks();
            model.getSongs().setAll(trackAssignerResult.getSongs());
            dialogState.getVisible().set(false);

            if (trackAssignerResult.getTrackIssue() != TrackIssue.NONE) {
                String message = buildTrackIssueMessage(trackAssignerResult.getTrackIssue());
                DialogManagerImpl.getInstance().showAlert(Alert.AlertType.INFORMATION , "Song track issue found!", message);
            }
            if (!result.getErrors().isEmpty()) {
                ErrorListAlertMessage message = new ErrorListAlertMessage("Song loading error", "Some songs have failed to load. See the errors bellow.", result.getErrors());
                DialogManagerImpl.getInstance().showErrorListAlert(message);
            }
        });
        songProcessor.setOnFailed(event -> {
            Throwable ex = songProcessor.getException();
            DialogManagerImpl.getInstance().showAlert(Alert.AlertType.ERROR , "Loading songs failed", "Loading songs from " + model.getSelectedPath() + " failed. " + ex);
            System.err.println("Song loading failed: " + ex);
            dialogState.getVisible().set(false);
        });

        new Thread(songProcessor).start();
        DialogManagerImpl.getInstance().showProgressDialog(dialogState);
    }

    private static String buildTrackIssueMessage(TrackIssue issue) {
        StringBuilder stringBuilder = new StringBuilder("Existing tracks were ignored because some songs have");
        if (issue == TrackIssue.DUPLICATE_TRACKS) {
            stringBuilder.append(" duplicate track numbers.");
        } else if (issue == TrackIssue.TRACKS_IN_INVALID_RANGE){
            stringBuilder.append(" track numbers outside the valid range.");
        }
        return stringBuilder.toString();
    }

    public Region getView() {
        Region build = viewBuilder.build();
        loadSongs();
        return build;
    }
}
