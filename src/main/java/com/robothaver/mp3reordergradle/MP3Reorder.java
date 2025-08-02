package com.robothaver.mp3reordergradle;

import atlantafx.base.theme.PrimerDark;
import com.robothaver.mp3reordergradle.mp3_viewer.MP3Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MP3Reorder extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        stage.setTitle("MP3 Reorder");
        stage.setScene(new Scene(new MP3Controller().getView()));
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }
}