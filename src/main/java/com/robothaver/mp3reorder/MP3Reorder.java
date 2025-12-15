package com.robothaver.mp3reorder;

import com.robothaver.mp3reorder.core.ApplicationInfo;
import com.robothaver.mp3reorder.core.font.FontSizeControllerImpl;
import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.preference.PreferenceStoreImpl;
import com.robothaver.mp3reorder.core.preference.Preferences;
import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.mp3.MP3Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MP3Reorder extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        DialogManagerImpl.initialize(stage);
        Preferences preferences = PreferenceStoreImpl.getInstance().getPreferences();
        Application.setUserAgentStylesheet(preferences.getSelectedTheme().getTheme().getUserAgentStylesheet());
        LanguageController.changeSelectedLocale(preferences.getSelectedLocale());
        stage.sceneProperty().addListener((_, _, newScene) -> {
            FontSizeControllerImpl.initialize(newScene.getRoot());
            FontSizeControllerImpl.getInstance().setFontSize(preferences.getSelectedSize());
        });
        stage.setTitle(ApplicationInfo.APPLICATION_NAME);
        stage.setScene(new Scene(new MP3Controller().getView()));
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }
}