package com.robothaver.mp3reorder.dialog.error;

import lombok.Data;

@Data
public class Error {
    private final String file;
    private final String message;
    private final Exception exception;
}
