package com.robothaver.mp3reorder.dialog.error;

import javafx.collections.ObservableList;
import lombok.Data;

@Data
public class ErrorListAlertMessage {
    private final String title;
    private final String message;
    private final ObservableList<Error> errors;
}
