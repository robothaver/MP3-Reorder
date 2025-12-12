package com.robothaver.mp3reorder.mp3.song.task.domain;

import lombok.Data;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@Data
public class SongTask<T> {
    private final Callable<T> callable;
    private final Path file;
}
