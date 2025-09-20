package com.robothaver.mp3reorder.mp3_viewer.controls.menubar;

import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import lombok.RequiredArgsConstructor;

import java.io.File;

@RequiredArgsConstructor
public class MenuBarInteractor {
    private final MenuBarModel model;
    private final MP3Model mp3Model;

    public void selectTheme(Themes theme) {
        model.getSelectedTheme().set(theme);
        Application.setUserAgentStylesheet(theme.getTheme().getUserAgentStylesheet());
    }

    public void setSize(Parent root, Size size) {
        model.getSelectedSize().set(size);
        root.setStyle("-fx-font-size: %dpx".formatted(size.getFontSize()));
    }

    public void openDirectory(Runnable onLoadSongs) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        directoryChooser.setTitle("Choose the Folder Containing Your Songs");
        File selectedDirectory = directoryChooser.showDialog(Window.getWindows().getFirst());

        if (selectedDirectory != null) {
            mp3Model.selectedPathProperty().setValue(selectedDirectory.getAbsolutePath());
            onLoadSongs.run();
        }
    }
}
