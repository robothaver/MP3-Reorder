package com.robothaver.mp3reorder.dialog;

import com.robothaver.mp3reorder.dialog.error.ErrorListAlertMessage;
import com.robothaver.mp3reorder.dialog.option.OptionDialogMessage;
import com.robothaver.mp3reorder.dialog.progress.ProgressDialogState;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.util.Optional;

public interface DialogManager {
    void showOkAlert(String title, String message);
    Optional<ButtonType> showOptionDialog(OptionDialogMessage optionDialogMessage);
    void showProgressDialog(ProgressDialogState state);
    void showErrorListAlert(ErrorListAlertMessage message);
    File showDirectoryChooserDialog(String title, File initialDirectory);
}
