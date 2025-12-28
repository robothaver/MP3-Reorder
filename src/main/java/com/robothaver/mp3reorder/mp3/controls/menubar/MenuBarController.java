package com.robothaver.mp3reorder.mp3.controls.menubar;

import com.robothaver.mp3reorder.core.BaseController;
import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.core.preference.PreferenceStoreImpl;
import com.robothaver.mp3reorder.core.preference.Preferences;
import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.dialog.error.ErrorListAlertMessage;
import com.robothaver.mp3reorder.dialog.progress.ProgressDialogState;
import com.robothaver.mp3reorder.dialog.progress.ProgressState;
import com.robothaver.mp3reorder.mp3.MP3Model;
import com.robothaver.mp3reorder.mp3.song.saver.SongSaverTaskProvider;
import com.robothaver.mp3reorder.mp3.song.task.SongTaskExecutor;
import com.robothaver.mp3reorder.mp3.song.task.domain.ProcessorResult;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import lombok.extern.log4j.Log4j2;

import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class MenuBarController extends BaseController<MenuBar> {
    private final MP3Model mp3Model;
    private final MenuBarModel menuBarModel;
    private final MenuBarInteractor interactor;
    private final ViewLocalization localization = new ViewLocalization("language.song_saver", LanguageController.getSelectedLocale());

    public MenuBarController(MP3Model mp3Model, Runnable loadSongs) {
        this.mp3Model = mp3Model;
        menuBarModel = mp3Model.getMenuBarModel();
        interactor = new MenuBarInteractor(mp3Model);
        viewBuilder = new MenuBarViewBuilder(
                menuBarModel,
                interactor::selectTheme,
                interactor::setSelectedLocale,
                interactor::setSize,
                () -> interactor.openDirectory(loadSongs),
                interactor::changeDetailsSideMenuEnabled,
                interactor::changeStatusBarEnabled,
                () -> System.exit(0),
                interactor::setTracksForSongsByFileName,
                interactor::removeIndexFromFileNames,
                this::onSave,
                this::onSaveAs
        );
        setupModel();
    }

    private void onSave() {
        SongSaverTaskProvider taskProvider = new SongSaverTaskProvider(mp3Model.getSongs());
        saveSongs(taskProvider);
    }

    private void onSaveAs() {
        Path saveLocation = interactor.getSaveLocation();
        if (saveLocation == null) return;

        if (!saveLocation.equals(Paths.get(mp3Model.getSelectedPath()))) {
            SongSaverTaskProvider taskProvider = new SongSaverTaskProvider(mp3Model.getSongs(), saveLocation);
            saveSongs(taskProvider);
        } else {
            // The currently open directory was selected
            onSave();
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

        taskExecutor.setOnSucceeded(_ -> {
            ProcessorResult<Void> result = taskExecutor.getValue();
            dialogState.getVisible().set(false);

            if (!result.getErrors().isEmpty()) {
                ErrorListAlertMessage message = new ErrorListAlertMessage(localization.getForKey("saving.error.title"), localization.getForKey("saving.error.message"), result.getErrors());
                DialogManagerImpl.getInstance().showErrorListAlert(message);
            } else {
                DialogManagerImpl.getInstance().showAlert(Alert.AlertType.INFORMATION, localization.getForKey("saving.finished.title"), localization.getForKey("saving.finished.message"));
            }
        });
        taskExecutor.setOnFailed(_ -> {
            Throwable ex = taskExecutor.getException();
            DialogManagerImpl.getInstance().showAlert(Alert.AlertType.ERROR, localization.getForKey("saving.failed.title"), localization.getForKey("saving.failed.message").formatted(mp3Model.getSelectedPath(), ex));
            log.error("Song saving failed: {}", String.valueOf(ex));
            dialogState.getVisible().set(false);
        });

        new Thread(taskExecutor).start();
        DialogManagerImpl.getInstance().showProgressDialog(dialogState);
    }

    private void setupModel() {
        Preferences preferences = PreferenceStoreImpl.getInstance().getPreferences();
        menuBarModel.getSelectedLocale().set(preferences.getSelectedLocale());
        menuBarModel.getSelectedTheme().set(preferences.getSelectedTheme());
        menuBarModel.getSelectedSize().set(preferences.getSelectedSize());
        menuBarModel.getDetailsMenuEnabled().set(preferences.isSideMenuEnabled());
        menuBarModel.getStatusBarEnabled().set(preferences.isStatusBarEnabled());
    }
}
