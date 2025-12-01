package com.robothaver.mp3reorder.mp3_viewer;

import com.robothaver.mp3reorder.core.BaseController;
import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.dialog.error.ErrorListAlertMessage;
import com.robothaver.mp3reorder.dialog.progress.ProgressDialogState;
import com.robothaver.mp3reorder.dialog.progress.ProgressState;
import com.robothaver.mp3reorder.mp3_viewer.domain.Song;
import com.robothaver.mp3reorder.mp3_viewer.song.loader.SongLoaderTaskProvider;
import com.robothaver.mp3reorder.mp3_viewer.song.task.SongTaskExecutor;
import com.robothaver.mp3reorder.mp3_viewer.song.task.domain.ProcessorResult;
import com.robothaver.mp3reorder.mp3_viewer.song.track.assigner.TrackAssigner;
import com.robothaver.mp3reorder.mp3_viewer.song.track.assigner.TrackAssignerImpl;
import com.robothaver.mp3reorder.mp3_viewer.song.track.assigner.TrackAssignerResult;
import com.robothaver.mp3reorder.mp3_viewer.song.track.assigner.TrackIssue;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;

import static com.robothaver.mp3reorder.core.ApplicationInfo.APPLICATION_NAME;

public class MP3Controller extends BaseController<Region> {
    private final MP3Model model;
    private final ViewLocalization songLoaderLocalization = new ViewLocalization("language.song_loader", LanguageController.getSelectedLocale());
    private final ViewLocalization trackAssignerLocalization = new ViewLocalization("language.song_track_assigner", LanguageController.getSelectedLocale());

    public MP3Controller() {
        this.model = new MP3Model();
        viewBuilder = new MP3ViewBuilder(
                model,
                this::loadSongs
        );
    }

    private void loadSongs() {
        ProgressState progressState = new ProgressState();
        ProgressDialogState dialogState = new ProgressDialogState(
                songLoaderLocalization.getForKey("loading.progress.dialog.title"),
                songLoaderLocalization.getForKey("loading.progress.dialog.message.prefix"),
                songLoaderLocalization.getForKey("loading.progress.dialog.message"),
                progressState
        );

        SongTaskExecutor<Song> songProcessor = new SongTaskExecutor<>(new SongLoaderTaskProvider(model.getSelectedPath()), progressState);
        songProcessor.setOnSucceeded(event -> {
            Stage stage = (Stage) Window.getWindows().getFirst();
            stage.setTitle(APPLICATION_NAME + " - " + model.getSelectedPath());
            ProcessorResult<Song> result = songProcessor.getValue();
            TrackAssigner trackAssigner = new TrackAssignerImpl(result.getResults());
            TrackAssignerResult trackAssignerResult = trackAssigner.assignTracks();
            model.getSongs().setAll(trackAssignerResult.getSongs());
            dialogState.getVisible().set(false);
            model.getSongSearch().clear();

            if (trackAssignerResult.getTrackIssue() != TrackIssue.NONE) {
                String message = buildTrackIssueMessage(trackAssignerResult.getTrackIssue());
                DialogManagerImpl.getInstance().showAlert(Alert.AlertType.INFORMATION, trackAssignerLocalization.getForKey("song.track.issue.title"), message);
            }
            if (!result.getErrors().isEmpty()) {
                ErrorListAlertMessage message = new ErrorListAlertMessage(songLoaderLocalization.getForKey("song.loading.error.title"), songLoaderLocalization.getForKey("song.loading.error.message"), result.getErrors());
                DialogManagerImpl.getInstance().showErrorListAlert(message);
            }
        });
        songProcessor.setOnFailed(event -> {
            Throwable ex = songProcessor.getException();
            DialogManagerImpl.getInstance().showAlert(Alert.AlertType.ERROR, songLoaderLocalization.getForKey("song.loading.failed.title"),  songLoaderLocalization.getForKey("song.loading.failed.message").formatted(model.getSelectedPath(), ex));
            System.err.println("Song loading failed: " + ex);
            dialogState.getVisible().set(false);
        });

        new Thread(songProcessor).start();
        DialogManagerImpl.getInstance().showProgressDialog(dialogState);
    }

    private String buildTrackIssueMessage(TrackIssue issue) {
        StringBuilder stringBuilder = new StringBuilder(trackAssignerLocalization.getForKey("error.base.message") + " ");
        if (issue == TrackIssue.DUPLICATE_TRACKS) {
            stringBuilder.append(trackAssignerLocalization.getForKey("error.duplicate.tracks"));
        } else if (issue == TrackIssue.TRACKS_IN_INVALID_RANGE) {
            stringBuilder.append(trackAssignerLocalization.getForKey("error.outside.range"));
        }
        return stringBuilder.toString();
    }

    @Override
    public Region getView() {
        Region build = viewBuilder.build();
        loadSongs();
        return build;
    }
}
