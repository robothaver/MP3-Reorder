package com.robothaver.mp3reorder.mp3_viewer.song.task;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class DefaultFutureCollector<T> implements FutureCollector<T> {
    @Override
    public T getFuture(CompletableFuture<T> future) throws ExecutionException, InterruptedException {
        return future.get();
    }
}
