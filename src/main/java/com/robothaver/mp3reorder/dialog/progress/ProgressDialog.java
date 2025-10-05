package com.robothaver.mp3reorder.dialog.progress;

import atlantafx.base.theme.Styles;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProgressDialog extends Stage {
    private final Stage parent;
    private final ProgressDialogState state;

    public ProgressDialog(Stage parent, ProgressDialogState state) {
        this.parent = parent;
        this.state = state;
        setupStage();
        createUI();
    }

    private void setupStage() {
        initOwner(parent);
        initModality(Modality.WINDOW_MODAL);
        setTitle(state.getTitle());
        setWidth(400);
        onCloseRequestProperty().set(Event::consume);
        state.getVisible().addListener((observable, oldValue, visible) -> {
            if (!visible) { close(); }
        });
    }

    private void createUI() {
        VBox root = new VBox();
        root.setSpacing(20);
        root.setPadding(new Insets(10, 10, 20, 10));

        Label messageLabel = new Label(state.getMessage());
        messageLabel.getStyleClass().add(Styles.TITLE_4);

        ProgressState progressState = state.getProgressState();
        VBox progressContainer = new VBox();
        progressContainer.setSpacing(5);
        Label loadingProgressLabel = new Label();
        loadingProgressLabel.setText(getProgressLabelText(state.getProgressMessagePrefix(), progressState.getDone().get(), progressState.getAllTask().get()));
        ProgressBar progressBar = new ProgressBar();
        progressBar.setMaxWidth(Double.MAX_VALUE);

        progressState.getAllTask().addListener(observable -> {
            loadingProgressLabel.setText(getProgressLabelText(state.getProgressMessagePrefix(), progressState.getDone().get(), progressState.getAllTask().get()));
            progressBar.setProgress(0);
        });

        progressState.getDone().addListener(observable -> {
            int done = progressState.getDone().get();
            int all = progressState.getAllTask().get();
            loadingProgressLabel.setText(getProgressLabelText(state.getProgressMessagePrefix(), progressState.getDone().get(), progressState.getAllTask().get()));
            progressBar.setProgress((double) done / all);
        });
        progressContainer.getChildren().addAll(loadingProgressLabel, progressBar);
        root.getChildren().addAll(messageLabel, progressContainer);
        setScene(new Scene(root));
    }

    private String getProgressLabelText(String prefix, int done, int all) {
        return prefix + " " + done + "/" + all;
    }
}
