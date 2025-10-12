package com.robothaver.mp3reorder.dialog;

import com.robothaver.mp3reorder.dialog.error.ErrorListAlert;
import com.robothaver.mp3reorder.dialog.error.ErrorListAlertMessage;
import com.robothaver.mp3reorder.dialog.progress.ProgressDialog;
import com.robothaver.mp3reorder.dialog.progress.ProgressDialogState;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

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
    public void showOkAlert(String title, String message) {
        parentStageSizeFix();
        new OkAlert(primaryStage, title, message).showAndWait();
    }

    @Override
    public Optional<ButtonType> showOptionDialog(OptionDialogMessage optionDialogMessage) {
        parentStageSizeFix();
        return new OptionDialog(primaryStage, optionDialogMessage).showAndWait();
    }

    @Override
    public void showProgressDialog(ProgressDialogState state) {
        parentStageSizeFix();
        new ProgressDialog(primaryStage, state).showAndWait();
    }

    @Override
    public void showErrorListAlert(ErrorListAlertMessage message) {
        parentStageSizeFix();
        new ErrorListAlert(primaryStage, message).showAndWait();
    }

    private void parentStageSizeFix() {
        // Fix for maximized window breaking on linux
        if (primaryStage.isMaximized()) {
            primaryStage.setWidth(primaryStage.getWidth());
            primaryStage.setHeight(primaryStage.getHeight());
        }
    }
}
