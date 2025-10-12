package com.robothaver.mp3reorder.dialog.option;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

public class OptionDialog extends Dialog<ButtonType> {
    public OptionDialog(Stage parentStage, OptionDialogMessage optionDialogMessage) {
        initOwner(parentStage);
        getDialogPane().setPadding(new Insets(0, 10, 0, 10));
        setTitle(optionDialogMessage.getTitle());
        setContentText(optionDialogMessage.getMessage());
        getDialogPane().getButtonTypes().addAll(optionDialogMessage.getOptions());
    }
}
