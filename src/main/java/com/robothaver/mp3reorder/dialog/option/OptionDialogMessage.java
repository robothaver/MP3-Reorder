package com.robothaver.mp3reorder.dialog.option;

import javafx.scene.control.ButtonType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class OptionDialogMessage {
    private final String message;
    private final String title;
    private final List<ButtonType> options;
}
