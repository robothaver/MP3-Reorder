package com.robothaver.mp3reorder.mp3_viewer.controls.menubar;

import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.dialog.error.ErrorListAlertMessage;
import com.robothaver.mp3reorder.dialog.progress.ProgressDialogState;
import com.robothaver.mp3reorder.dialog.progress.ProgressState;
import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.mp3_viewer.song.saver.SongSaverTaskProvider;
import com.robothaver.mp3reorder.mp3_viewer.song.task.SongTaskExecutor;
import com.robothaver.mp3reorder.mp3_viewer.song.task.domain.ProcessorResult;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.util.Builder;

public class MenuBarController {
    private final MenuBarModel model;
    private final Builder<MenuBar> viewBuilder;
    private final MenuBarInteractor interactor;

    public MenuBarController(MP3Model mp3Model, Runnable loadSongs) {
        this.model = new MenuBarModel();
        interactor = new MenuBarInteractor(model, mp3Model);
        viewBuilder = new MenuBarViewBuilder(
                model,
                mp3Model.getDetailsMenuEnabled(),
                interactor::selectTheme,
                interactor::setSize,
                () -> interactor.openDirectory(loadSongs),
                () -> System.exit(0),
                interactor::setTracksForSongsByFileName,
                interactor::removeIndexFromFileNames,
                () -> {
                    ProgressState progressState = new ProgressState();
                    ProgressDialogState dialogState = new ProgressDialogState(
                            "Saving songs",
                            "Songs saved: ",
                            "Saving songs...",
                            progressState
                    );

                    SongSaverTaskProvider taskProvider = new SongSaverTaskProvider(mp3Model.getSongs());
                    SongTaskExecutor<Void> taskExecutor = new SongTaskExecutor<>(taskProvider, progressState);

                    taskExecutor.setOnSucceeded(event -> {
                        ProcessorResult<Void> result = taskExecutor.getValue();
                        dialogState.getVisible().set(false);

                        if (!result.getErrors().isEmpty()) {
                            ErrorListAlertMessage message = new ErrorListAlertMessage("Song saving error", "Some songs have failed to save. See the errors bellow.", result.getErrors());
                            DialogManagerImpl.getInstance().showErrorListAlert(message);
                        } else {
                            DialogManagerImpl.getInstance().showAlert(Alert.AlertType.INFORMATION, "Saving songs finished", "All songs have been saved successfully!");
                        }
                    });
                    taskExecutor.setOnFailed(event -> {
                        Throwable ex = taskExecutor.getException();
                        DialogManagerImpl.getInstance().showAlert(Alert.AlertType.ERROR , "Saving songs failed", "Saving songs to " + mp3Model.getSelectedPath() + " failed. " + ex);
                        System.err.println("Song saving failed: " + ex);
                        ex.printStackTrace();
                        dialogState.getVisible().set(false);
                    });

                    new Thread(taskExecutor).start();
                    DialogManagerImpl.getInstance().showProgressDialog(dialogState);
                }
        );
    }

    public MenuBar getView() {
        return viewBuilder.build();
    }
}
