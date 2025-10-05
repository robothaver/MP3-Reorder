package com.robothaver.mp3reorder.dialog.progress;

import javafx.beans.property.*;
import lombok.Data;

@Data
public class ProgressDialogState {
    private final String title;
    private final String progressMessagePrefix;
    private final String message;
    private final BooleanProperty visible = new SimpleBooleanProperty(true);
    private final ProgressState progressState;
}
