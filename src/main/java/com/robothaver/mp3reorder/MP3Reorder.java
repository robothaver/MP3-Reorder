package com.robothaver.mp3reorder;

import com.robothaver.mp3reorder.core.ApplicationInfo;
import com.robothaver.mp3reorder.core.LoggerConfig;
import com.robothaver.mp3reorder.core.font.FontSizeControllerImpl;
import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.preference.PreferenceStoreImpl;
import com.robothaver.mp3reorder.core.preference.Preferences;
import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.start.StartController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class MP3Reorder extends Application {
    static {
        LoggerConfig.setLoggerDirectory();
    }

    static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        DialogManagerImpl.initialize(stage);
        Preferences preferences = PreferenceStoreImpl.getInstance().getPreferences();
        if (preferences.isLaunchMaximized()) stage.setMaximized(true);
        Application.setUserAgentStylesheet(preferences.getSelectedTheme().getTheme().getUserAgentStylesheet());
        LanguageController.changeSelectedLocale(preferences.getSelectedLocale());

        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png"))));
        stage.sceneProperty().addListener((_, _, newScene) -> {
            FontSizeControllerImpl.initialize(newScene.getRoot());
            FontSizeControllerImpl.getInstance().setFontSize(preferences.getSelectedSize());
            newScene.getStylesheets().add(Objects.requireNonNull(MP3Reorder.class.getResource("/styles.css")).toString());
        });
        stage.setTitle(ApplicationInfo.APPLICATION_NAME);
        stage.setScene(new Scene(new StartController().getView()));
        //MP3Controller mp3Controller = new MP3Controller();
        //stage.setScene(new Scene(mp3Controller.getView()));
        //mp3Controller.loadSongs(Path.of("bulira  zenék"));
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }
}