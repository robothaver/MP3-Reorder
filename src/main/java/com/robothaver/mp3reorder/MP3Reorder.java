package com.robothaver.mp3reorder;

import atlantafx.base.theme.PrimerDark;
import com.robothaver.mp3reorder.core.ApplicationInfo;
import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.mp3_viewer.MP3Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MP3Reorder extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        DialogManagerImpl.init(stage);
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        LanguageController.changeSelectedLocale("hu");
        stage.setTitle(ApplicationInfo.APPLICATION_NAME);
        stage.setScene(new Scene(new MP3Controller().getView()));
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }
}