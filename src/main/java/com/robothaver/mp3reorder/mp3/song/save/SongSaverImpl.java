package com.robothaver.mp3reorder.mp3.song.save;

import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.dialog.error.ErrorListAlertMessage;
import com.robothaver.mp3reorder.dialog.progress.ProgressDialogState;
import com.robothaver.mp3reorder.dialog.progress.ProgressState;
import com.robothaver.mp3reorder.mp3.domain.Song;
import com.robothaver.mp3reorder.mp3.song.save.task.SongSaverTaskProvider;
import com.robothaver.mp3reorder.mp3.song.task.SongTaskExecutor;
import com.robothaver.mp3reorder.mp3.song.task.domain.ProcessorResult;
import javafx.scene.control.Alert;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.nio.file.Path;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class SongSaverImpl implements SongSaver {
    private final Path currentPath;
    private final Path savePath;
    private final List<Song> songs;
    private final ViewLocalization localization = new ViewLocalization("language.song_saver", LanguageController.getSelectedLocale());

    @Override
    public void save() {
        SongSaverTaskProvider taskProvider;
        if (!savePath.equals(currentPath)) {
            taskProvider = new SongSaverTaskProvider(songs, savePath);
        } else {
            // The currently open directory was selected
            taskProvider = new SongSaverTaskProvider(songs);
        }
        saveSongs(taskProvider);
    }

    private void saveSongs(SongSaverTaskProvider taskProvider) {
        log.info("Saving {} songs to {}", songs.size(), savePath);
        ProgressState progressState = new ProgressState();
        ProgressDialogState dialogState = new ProgressDialogState(
                localization.getForKey("saving.progress.dialog.title"),
                localization.getForKey("saving.progress.dialog.message.prefix"),
                localization.getForKey("saving.progress.dialog.message"),
                progressState
        );

        SongTaskExecutor<Void> taskExecutor = new SongTaskExecutor<>(taskProvider, progressState);

        taskExecutor.setOnSucceeded(_ -> {
            ProcessorResult<Void> result = taskExecutor.getValue();
            dialogState.getVisible().set(false);

            if (!result.getErrors().isEmpty()) {
                log.error("Failed to save {} songs", result.getErrors().size());
                ErrorListAlertMessage message = new ErrorListAlertMessage(localization.getForKey("saving.error.title"), localization.getForKey("saving.error.message"), result.getErrors());
                DialogManagerImpl.getInstance().showErrorListAlert(message);
            } else {
                log.info("All songs saved successfully");
                DialogManagerImpl.getInstance().showAlert(Alert.AlertType.INFORMATION, localization.getForKey("saving.finished.title"), localization.getForKey("saving.finished.message"));
            }
        });
        taskExecutor.setOnFailed(_ -> {
            Throwable ex = taskExecutor.getException();
            DialogManagerImpl.getInstance().showAlert(Alert.AlertType.ERROR, localization.getForKey("saving.failed.title"), localization.getForKey("saving.failed.message").formatted(savePath, ex));
            log.error("Song saving failed: {}", String.valueOf(ex));
            dialogState.getVisible().set(false);
        });

        new Thread(taskExecutor).start();
        DialogManagerImpl.getInstance().showProgressDialog(dialogState);
    }
}
