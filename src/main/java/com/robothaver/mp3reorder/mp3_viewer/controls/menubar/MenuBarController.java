package com.robothaver.mp3reorder.mp3_viewer.controls.menubar;

import com.robothaver.mp3reorder.core.BaseController;
import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.dialog.error.ErrorListAlertMessage;
import com.robothaver.mp3reorder.dialog.progress.ProgressDialogState;
import com.robothaver.mp3reorder.dialog.progress.ProgressState;
import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.mp3_viewer.song.saver.SongSaverTaskProvider;
import com.robothaver.mp3reorder.mp3_viewer.song.task.SongTaskExecutor;
import com.robothaver.mp3reorder.mp3_viewer.song.task.domain.ProcessorResult;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;

import java.nio.file.Path;

public class MenuBarController extends BaseController<MenuBar> {
    private final MP3Model mp3Model;
    private final MenuBarInteractor interactor;
    private final ViewLocalization localization = new ViewLocalization("language.song_saver", LanguageController.getSelectedLocale());

    public MenuBarController(MP3Model mp3Model, Runnable loadSongs) {
        this.mp3Model = mp3Model;
        MenuBarModel model = new MenuBarModel();
        interactor = new MenuBarInteractor(model, mp3Model);
        viewBuilder = new MenuBarViewBuilder(
                model,
                mp3Model.getDetailsMenuEnabled(),
                mp3Model.getStatusBarEnabled(),
                interactor::selectTheme,
                interactor::setSize,
                () -> interactor.openDirectory(loadSongs),
                () -> System.exit(0),
                interactor::setTracksForSongsByFileName,
                interactor::removeIndexFromFileNames,
                this::onSave,
                this::onSaveAs
        );
    }

    private void onSave() {
        SongSaverTaskProvider taskProvider = new SongSaverTaskProvider(mp3Model.getSongs());
        saveSongs(taskProvider);
    }

    private void onSaveAs() {
        Path saveLocation = interactor.getSaveLocation();
        if (saveLocation != null) {
            SongSaverTaskProvider taskProvider = new SongSaverTaskProvider(mp3Model.getSongs(), saveLocation);
            saveSongs(taskProvider);
        }
    }

    private void saveSongs(SongSaverTaskProvider taskProvider) {
        ProgressState progressState = new ProgressState();
        ProgressDialogState dialogState = new ProgressDialogState(
                localization.getForKey("saving.progress.dialog.title"),
                localization.getForKey("saving.progress.dialog.message.prefix"),
                localization.getForKey("saving.progress.dialog.message"),
                progressState
        );

        SongTaskExecutor<Void> taskExecutor = new SongTaskExecutor<>(taskProvider, progressState);

        taskExecutor.setOnSucceeded(event -> {
            ProcessorResult<Void> result = taskExecutor.getValue();
            dialogState.getVisible().set(false);

            if (!result.getErrors().isEmpty()) {
                ErrorListAlertMessage message = new ErrorListAlertMessage(localization.getForKey("saving.error.title"), localization.getForKey("saving.error.message"), result.getErrors());
                DialogManagerImpl.getInstance().showErrorListAlert(message);
            } else {
                DialogManagerImpl.getInstance().showAlert(Alert.AlertType.INFORMATION, localization.getForKey("saving.finished.title"), localization.getForKey("saving.finished.message"));
            }
        });
        taskExecutor.setOnFailed(event -> {
            Throwable ex = taskExecutor.getException();
            DialogManagerImpl.getInstance().showAlert(Alert.AlertType.ERROR, localization.getForKey("saving.failed.title"),  localization.getForKey("saving.failed.message").formatted(mp3Model.getSelectedPath(), ex));
            System.err.println("Song saving failed: " + ex);
            ex.printStackTrace();
            dialogState.getVisible().set(false);
        });

        new Thread(taskExecutor).start();
        DialogManagerImpl.getInstance().showProgressDialog(dialogState);
    }
}
