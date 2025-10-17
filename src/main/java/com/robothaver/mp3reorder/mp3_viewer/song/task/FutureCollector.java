package com.robothaver.mp3reorder.mp3_viewer.song.task;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface FutureCollector<T> {
    T getFuture(CompletableFuture<T> future) throws ExecutionException, InterruptedException;
}
