package com.robothaver.mp3reorder.mp3_viewer.song.task;

import com.robothaver.mp3reorder.dialog.error.Error;
import com.robothaver.mp3reorder.dialog.progress.ProgressState;
import com.robothaver.mp3reorder.mp3_viewer.song.task.domain.SongTask;
import com.robothaver.mp3reorder.mp3_viewer.song.task.domain.ProcessorResult;
import javafx.application.Platform;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

@RequiredArgsConstructor
public class SongTaskExecutor<T> extends Task<ProcessorResult<T>> {
    private final SongTaskProvider<T> songTaskProvider;
    private final ProgressState progressState;
    private final FutureCollector<T> futureCollector = new DefaultFutureCollector<>();
    private final List<Error> errors = new ArrayList<>();

    @Override
    protected ProcessorResult<T> call() throws Exception {
        List<SongTask<T>> tasks = songTaskProvider.getTasks();

        Platform.runLater(() -> progressState.allTaskProperty().setValue(tasks.size()));

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<T> results = tasks.stream()
                    .map(task -> CompletableFuture.supplyAsync(() -> callCallable(task), executor))
                    .map(this::collectFuture)
                    .filter(Objects::nonNull)
                    .toList();

            return new ProcessorResult<>(results, errors);
        }
    }

    private T collectFuture(CompletableFuture<T> future) {
        try {
            return futureCollector.getFuture(future);
        } catch (InterruptedException | ExecutionException e) {
            errors.add(new Error("", e.getMessage(), e));
            return null;
        }
    }

    private T callCallable(SongTask<T> songTask) {
        try {
            T call = songTask.getCallable().call();
            Platform.runLater(() -> progressState.doneProperty().set(progressState.getDone() + 1));
            return call;
        } catch (Exception e) {
            errors.add(new Error(songTask.getFile().getFileName().toString(), e.getMessage(), e));
        }
        return null;
    }
}
