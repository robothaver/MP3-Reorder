package com.robothaver.mp3reorder.dialog;

import com.robothaver.mp3reorder.dialog.error.ErrorListAlertMessage;
import com.robothaver.mp3reorder.dialog.option.OptionDialogMessage;
import com.robothaver.mp3reorder.dialog.progress.ProgressDialogState;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.util.Optional;

public interface DialogManager {
    void showAlert(Alert.AlertType type, String title, String message);
    Optional<ButtonType> showOptionDialog(OptionDialogMessage optionDialogMessage);
    void showProgressDialog(ProgressDialogState state);
    void showErrorListAlert(ErrorListAlertMessage message);
    File showDirectoryChooserDialog(String title, File initialDirectory);
}
