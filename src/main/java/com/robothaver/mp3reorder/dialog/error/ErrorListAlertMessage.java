package com.robothaver.mp3reorder.dialog.error;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;

import java.util.List;

@Data
public class ErrorListAlertMessage {
    private final String title;
    private final String message;
    private final ObservableList<Error> errors;

    public ErrorListAlertMessage(String title, String message, List<Error> errors) {
        this.title = title;
        this.message = message;
        this.errors = FXCollections.observableArrayList(errors);
    }
}
