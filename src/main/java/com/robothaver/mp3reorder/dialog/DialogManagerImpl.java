package com.robothaver.mp3reorder.dialog;

import com.robothaver.mp3reorder.dialog.error.ErrorListAlert;
import com.robothaver.mp3reorder.dialog.error.ErrorListAlertMessage;
import com.robothaver.mp3reorder.dialog.progress.ProgressDialog;
import com.robothaver.mp3reorder.dialog.progress.ProgressDialogState;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
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
    public void showOkDialog(String message, String title) {

    }

    @Override
    public Optional<ButtonType> showOptionDialog(OptionDialogMessage optionDialogMessage) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(primaryStage);
        dialog.getDialogPane().setStyle("-fx-padding: 0px 0px 0px 10px");
        dialog.setTitle(optionDialogMessage.getTitle());
        dialog.setContentText(optionDialogMessage.getMessage());
        dialog.getDialogPane().getButtonTypes().addAll(optionDialogMessage.getOptions());
        // Fix for maximized window breaking on linux
        if (primaryStage.isMaximized()) {
            primaryStage.setWidth(primaryStage.getWidth());
            primaryStage.setHeight(primaryStage.getHeight());
        }
        return dialog.showAndWait();
    }

    @Override
    public void showProgressDialog(ProgressDialogState state) {
        new ProgressDialog(primaryStage, state).showAndWait();
    }

    @Override
    public void showErrorListAlert(ErrorListAlertMessage message) {
        new ErrorListAlert(primaryStage, message).showAndWait();
    }
}
