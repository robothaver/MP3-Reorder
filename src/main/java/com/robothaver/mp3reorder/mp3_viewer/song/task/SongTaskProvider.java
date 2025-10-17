package com.robothaver.mp3reorder.mp3_viewer.song.task;

import com.robothaver.mp3reorder.mp3_viewer.song.task.domain.SongTask;

import java.util.List;

public interface SongTaskProvider<T> {
    List<SongTask<T>> getTasks() throws Exception;
}
