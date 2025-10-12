package com.robothaver.mp3reorder.dialog;

import com.robothaver.mp3reorder.dialog.error.ErrorListAlert;
import com.robothaver.mp3reorder.dialog.error.ErrorListAlertMessage;
import com.robothaver.mp3reorder.dialog.option.OptionDialog;
import com.robothaver.mp3reorder.dialog.option.OptionDialogMessage;
import com.robothaver.mp3reorder.dialog.progress.ProgressDialog;
import com.robothaver.mp3reorder.dialog.progress.ProgressDialogState;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class DialogManagerImpl implements DialogManager {
    private static DialogManager instance;
    private final Stage primaryStage;

    public DialogManagerImpl(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public static void init(Stage stage) {
        instance = new DialogManagerImpl(stage);
    }

    public static DialogManager getInstance() {
        if (instance == null) throw new IllegalStateException("Dialog manager has not been initialized!");
        return instance;
    }

    @Override
    public void showAlert(Alert.AlertType type, String title, String message) {
        Platform.runLater(() -> {
            parentStageSizeFix();
            new OkAlert(primaryStage, type, title, message).showAndWait();
        });
    }

    @Override
    public Optional<ButtonType> showOptionDialog(OptionDialogMessage optionDialogMessage) {
        parentStageSizeFix();
        return new OptionDialog(primaryStage, optionDialogMessage).showAndWait();
    }

    @Override
    public void showProgressDialog(ProgressDialogState state) {
        Platform.runLater(() -> {
            parentStageSizeFix();
            new ProgressDialog(primaryStage, state).showAndWait();
        });
    }

    @Override
    public void showErrorListAlert(ErrorListAlertMessage message) {
        Platform.runLater(() -> {
            parentStageSizeFix();
            new ErrorListAlert(primaryStage, message).showAndWait();
        });
    }

    @Override
    public File showDirectoryChooserDialog(String title, File initialDirectory) {
        DirectoryChooserDialog dialog = new DirectoryChooserDialog(primaryStage, title, initialDirectory);
        parentStageSizeFix();
        return dialog.showDialog();
    }

    private void parentStageSizeFix() {
        // Fix for maximized window breaking on linux
        if (primaryStage.isMaximized()) {
            primaryStage.setWidth(primaryStage.getWidth());
            primaryStage.setHeight(primaryStage.getHeight());
        }
    }
}
