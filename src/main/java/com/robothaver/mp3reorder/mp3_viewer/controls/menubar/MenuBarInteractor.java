package com.robothaver.mp3reorder.mp3_viewer.controls.menubar;

import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.mp3_viewer.domain.Song;
import com.robothaver.mp3reorder.mp3_viewer.utils.MP3FileUtils;
import javafx.application.Application;
import javafx.scene.Parent;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.robothaver.mp3reorder.mp3_viewer.utils.MP3FileUtils.getTrackNumberFromFileName;

@RequiredArgsConstructor
public class MenuBarInteractor {
    private final MenuBarModel model;
    private final MP3Model mp3Model;
    private final ViewLocalization localization = new ViewLocalization("language.menubar", LanguageController.getSelectedLocale());

    public Path getSaveLocation() {
        File selectedDirectory = DialogManagerImpl.getInstance()
                .showDirectoryChooserDialog(localization.getForKey("file.saveAsDialogTitle"), new File("."));
        if (selectedDirectory != null) {
            return Paths.get(selectedDirectory.getAbsolutePath());
        }
        return null;
    }

    public void selectTheme(Themes theme) {
        model.getSelectedTheme().set(theme);
        Application.setUserAgentStylesheet(theme.getTheme().getUserAgentStylesheet());
    }

    public void setSize(Parent root, Size size) {
        model.getSelectedSize().set(size);
        root.setStyle("-fx-font-size: %dpx".formatted(size.getFontSize()));
    }

    public void openDirectory(Runnable onLoadSongs) {
        File selectedDirectory = DialogManagerImpl.getInstance()
                .showDirectoryChooserDialog(localization.getForKey("file.openDialogTitle"), new File("."));

        if (selectedDirectory != null) {
            mp3Model.selectedPathProperty().setValue(selectedDirectory.getAbsolutePath());
            onLoadSongs.run();
        }
    }

    public void setTracksForSongsByFileName() {
        List<Song> songs = mp3Model.getSongs();
        songs.sort((o1, o2) -> MP3FileUtils.compareFileNames(o2.getFileName(), o1.getFileName()));
        for (int i = 0; i < songs.size(); i++) {
            songs.get(i).trackProperty().setValue(i + 1);
        }
    }

    public void removeIndexFromFileNames() {
        List<Song> songs = mp3Model.getSongs();
        for (Song song : songs) {
            int track = getTrackNumberFromFileName(song.getFileName());
            if (track != -1) {
                String newFileName = song.getFileName().replace(String.valueOf(track), "").trim();
                boolean duplicate = false;
                for (Song song1 : songs) {
                    if (song1.getFileName().equals(newFileName)) {
                        duplicate = true;
                        break;
                    }
                }
                if (!duplicate) {
                    song.fileNameProperty().setValue(newFileName);
                }
            }
        }
        mp3Model.getSongSearch().clear();
    }
}
