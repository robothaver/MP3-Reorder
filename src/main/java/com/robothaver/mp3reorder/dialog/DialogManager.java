package com.robothaver.mp3reorder.dialog;

import com.robothaver.mp3reorder.dialog.progress.ProgressDialogState;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public interface DialogManager {
    void showOkDialog(String message, String title);
    Optional<ButtonType> showOptionDialog(OptionDialogMessage optionDialogMessage);
    void showProgressDialog(ProgressDialogState state);
}
