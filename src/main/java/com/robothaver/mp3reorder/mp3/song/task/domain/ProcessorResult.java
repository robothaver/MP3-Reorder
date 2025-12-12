package com.robothaver.mp3reorder.mp3.song.task.domain;

import com.robothaver.mp3reorder.dialog.error.Error;
import lombok.Data;

import java.util.List;

@Data
public class ProcessorResult<T> {
    private final List<T> results;
    private final List<Error> errors;
}
