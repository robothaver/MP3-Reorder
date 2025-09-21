package com.robothaver.mp3reorder.mp3_viewer.controls.menubar;

import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import com.robothaver.mp3reorder.mp3_viewer.utils.MP3FileUtils;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.List;

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

    public void setTracksForSongsByFileName() {
        List<Song> songs = mp3Model.getSongs();
        songs.sort((o1, o2) -> MP3FileUtils.compareFileNames(o2.getFileName(), o1.getFileName()));
        for (int i = 0; i < songs.size(); i++) {
            songs.get(i).setTrack(i + 1);
        }
    }
}
