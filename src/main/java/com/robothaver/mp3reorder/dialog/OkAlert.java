package com.robothaver.mp3reorder.dialog;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class OkAlert extends Alert {
    private final Stage parentStage;
    private final String title;
    private final String message;

    public OkAlert(Stage parentStage, AlertType type, String title, String message) {
        super(type);
        this.parentStage = parentStage;
        this.title = title;
        this.message = message;
        setupAlert();
    }

    private void setupAlert() {
        setResizable(true);
        setTitle(title);
        setHeaderText(null);
        setContentText(message);
        initOwner(parentStage);
    }
}
