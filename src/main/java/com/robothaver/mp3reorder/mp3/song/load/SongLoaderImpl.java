package com.robothaver.mp3reorder.mp3.song.load;

import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.dialog.error.Error;
import com.robothaver.mp3reorder.dialog.error.ErrorListAlertMessage;
import com.robothaver.mp3reorder.dialog.progress.ProgressDialogState;
import com.robothaver.mp3reorder.dialog.progress.ProgressState;
import com.robothaver.mp3reorder.mp3.MP3Model;
import com.robothaver.mp3reorder.mp3.MP3ViewBuilder;
import com.robothaver.mp3reorder.mp3.domain.Song;
import com.robothaver.mp3reorder.mp3.song.load.task.SongLoaderTaskProvider;
import com.robothaver.mp3reorder.mp3.song.task.SongTaskExecutor;
import com.robothaver.mp3reorder.mp3.song.task.domain.ProcessorResult;
import com.robothaver.mp3reorder.mp3.song.track.assigner.TrackAssigner;
import com.robothaver.mp3reorder.mp3.song.track.assigner.TrackAssignerImpl;
import com.robothaver.mp3reorder.mp3.song.track.assigner.TrackAssignerResult;
import com.robothaver.mp3reorder.mp3.song.track.assigner.TrackIssue;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import static com.robothaver.mp3reorder.core.ApplicationInfo.APPLICATION_NAME;

@Log4j2
@RequiredArgsConstructor
public class SongLoaderImpl implements SongLoader {
    private final MP3Model model;
    private final Builder<Region> viewBuilder;
    private final ViewLocalization songLoaderLocalization = new ViewLocalization("language.song_loader", LanguageController.getSelectedLocale());
    private final ViewLocalization trackAssignerLocalization = new ViewLocalization("language.song_track_assigner", LanguageController.getSelectedLocale());

    @Override
    public void loadSongs() {
        log.info("Loading songs from {}", model.getSelectedPath());
        ProgressState progressState = new ProgressState();
        ProgressDialogState dialogState = createProgressDialogState(progressState);

        SongTaskExecutor<Song> songProcessor = new SongTaskExecutor<>(new SongLoaderTaskProvider(model.getSelectedPath()), progressState);
        songProcessor.setOnSucceeded(_ -> onLoadingSuccess(songProcessor, dialogState));
        songProcessor.setOnFailed(_ -> onLoadingFailed(songProcessor, dialogState));

        model.selectedSongIndexProperty().set(-1);
        DialogManagerImpl.getInstance().showProgressDialog(dialogState);
        new Thread(songProcessor).start();
    }

    private void onLoadingSuccess(SongTaskExecutor<Song> songProcessor, ProgressDialogState dialogState) {
        Stage stage = (Stage) Window.getWindows().getFirst();
        stage.setTitle(APPLICATION_NAME + " - " + model.getSelectedPath());

        // Process result
        ProcessorResult<Song> result = songProcessor.getValue();
        log.info("Successfully loaded {} songs", result.getResults().size());
        TrackAssigner trackAssigner = new TrackAssignerImpl(result.getResults());
        TrackAssignerResult trackAssignerResult = trackAssigner.assignTracks();
        model.getSongs().setAll(trackAssignerResult.getSongs());

        if (!model.getSongs().isEmpty()) {
            model.selectedSongIndexProperty().set(0);
            ((MP3ViewBuilder) viewBuilder).selectIndex(0);
        }

        showTrackIssueDialog(trackAssignerResult.getTrackIssue());
        showFailedSongsDialog(result.getErrors());

        model.getSongSearch().clear();
        dialogState.getVisible().set(false);
    }

    private void onLoadingFailed(SongTaskExecutor<Song> songProcessor, ProgressDialogState dialogState) {
        Throwable ex = songProcessor.getException();
        DialogManagerImpl.getInstance().showAlert(Alert.AlertType.ERROR, songLoaderLocalization.getForKey("song.loading.failed.title"), songLoaderLocalization.getForKey("song.loading.failed.message").formatted(model.getSelectedPath(), ex));
        log.error("Song loading failed", ex);
        dialogState.getVisible().set(false);
    }

    private void showFailedSongsDialog(List<Error> errors) {
        if (!errors.isEmpty()) {
            ErrorListAlertMessage message = new ErrorListAlertMessage(songLoaderLocalization.getForKey("song.loading.error.title"), songLoaderLocalization.getForKey("song.loading.error.message"), errors);
            DialogManagerImpl.getInstance().showErrorListAlert(message);
        }
    }

    private ProgressDialogState createProgressDialogState(ProgressState progressState) {
        return new ProgressDialogState(
                songLoaderLocalization.getForKey("loading.progress.dialog.title"),
                songLoaderLocalization.getForKey("loading.progress.dialog.message.prefix"),
                songLoaderLocalization.getForKey("loading.progress.dialog.message"),
                progressState
        );
    }

    private void showTrackIssueDialog(TrackIssue trackIssue) {
        if (trackIssue != TrackIssue.NONE) {
            log.warn("Existing tracks ignored because {}", trackIssue);
            String message = buildTrackIssueMessage(trackIssue);
            DialogManagerImpl.getInstance().showAlert(Alert.AlertType.INFORMATION, trackAssignerLocalization.getForKey("song.track.issue.title"), message);
        } else {
            log.info("No track issues found");
        }
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
}
