package com.robothaver.mp3reorder.mp3.controls.menubar;

import com.robothaver.mp3reorder.core.font.FontSizeControllerImpl;
import com.robothaver.mp3reorder.core.font.Size;
import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.preference.PreferenceStoreImpl;
import com.robothaver.mp3reorder.core.preference.Preferences;
import com.robothaver.mp3reorder.core.preference.PreferencesStore;
import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.mp3.MP3Model;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.mp3.domain.Song;
import com.robothaver.mp3reorder.mp3.utils.MP3FileUtils;
import javafx.application.Application;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

import static com.robothaver.mp3reorder.mp3.utils.MP3FileUtils.getTrackNumberFromFileName;

@RequiredArgsConstructor
public class MenuBarInteractor {
    private final MenuBarModel model;
    private final MP3Model mp3Model;
    private final ViewLocalization localization = new ViewLocalization("language.menubar", LanguageController.getSelectedLocale());
    private PreferencesStore<Preferences> preferencesStore = PreferenceStoreImpl.getInstance();

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
        preferencesStore.getPreferences().setSelectedTheme(theme);
        preferencesStore.savePreferences();
    }

    public void setSize(Size size) {
        model.getSelectedSize().set(size);
        FontSizeControllerImpl.getInstance().setFontSize(size);
        preferencesStore.getPreferences().setSelectedSize(size);
        preferencesStore.savePreferences();
    }

    public void setSelectedLocale(Locale locale) {
        LanguageController.changeSelectedLocale(locale);
        model.getSelectedLocale().set(locale);
        preferencesStore.getPreferences().setSelectedLocale(locale);
        preferencesStore.savePreferences();
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
