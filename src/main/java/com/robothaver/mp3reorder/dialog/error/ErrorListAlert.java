package com.robothaver.mp3reorder.dialog.error;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorListAlert extends Alert {
    private final ErrorListAlertMessage message;
    private final Stage parentStage;

    public ErrorListAlert(Stage parentStage, ErrorListAlertMessage message) {
        super(AlertType.ERROR);
        this.message = message;
        this.parentStage = parentStage;
        setupAlert();
        buildUI();
    }

    private void setupAlert() {
        setResizable(true);
        setTitle(message.getTitle());
        setHeaderText(message.getMessage());
        initOwner(parentStage);
    }

    private void buildUI() {
        VBox root = new VBox();
        root.setPadding(new Insets(10));

        TableView<Error> errorTableView = new TableView<>();
        errorTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);
        errorTableView.setPadding(new Insets(0));
        VBox.setVgrow(errorTableView, Priority.ALWAYS);
        Styles.toggleStyleClass(errorTableView, Styles.BORDERED);

        errorTableView.setItems(message.getErrors());
        TableColumn<Error, String> errorTitleColumn = new TableColumn<>("Title");
        errorTitleColumn.setCellValueFactory(new PropertyValueFactory<>("file"));

        TableColumn<com.robothaver.mp3reorder.dialog.error.Error, String> exceptionColumn = new TableColumn<>("Exception");
        exceptionColumn.setCellValueFactory(new PropertyValueFactory<>("exception"));

        TableColumn<Error, String> messageColumn = new TableColumn<>("Message");
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));

        errorTableView.getColumns().add(errorTitleColumn);
        errorTableView.getColumns().add(exceptionColumn);
        errorTableView.getColumns().add(messageColumn);

        TitledPane titledPane = new TitledPane();

        TextArea stacktraceTextArea = new TextArea();
        errorTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            newValue.getException().printStackTrace(printWriter);
            stacktraceTextArea.setText(stringWriter.toString());
        });

        titledPane.setContent(stacktraceTextArea);
        titledPane.setText("Stacktrace");
        Accordion accordion = new Accordion(titledPane);
        root.getChildren().addAll(errorTableView, accordion);

        getDialogPane().setContent(root);
        errorTableView.getSelectionModel().selectFirst();
    }
}
