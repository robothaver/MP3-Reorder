package com.robothaver.mp3reorder.mp3.song.task;

import com.robothaver.mp3reorder.mp3.song.task.domain.SongTask;

import java.io.IOException;
import java.util.List;

public interface SongTaskProvider<T> {
    List<SongTask<T>> getTasks() throws IOException;
}
