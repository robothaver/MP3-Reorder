package com.robothaver.mp3reorder.mp3.controls.menubar;

import com.robothaver.mp3reorder.core.BaseController;
import com.robothaver.mp3reorder.core.preference.PreferenceStoreImpl;
import com.robothaver.mp3reorder.core.preference.Preferences;
import com.robothaver.mp3reorder.mp3.MP3Model;
import com.robothaver.mp3reorder.mp3.song.save.SongSaver;
import com.robothaver.mp3reorder.mp3.song.save.SongSaverImpl;
import javafx.scene.control.MenuBar;
import lombok.extern.log4j.Log4j2;

import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class MenuBarController extends BaseController<MenuBar> {
    private final MP3Model mp3Model;
    private final MenuBarModel menuBarModel;
    private final MenuBarInteractor interactor;

    public MenuBarController(MP3Model mp3Model, Runnable loadSongs) {
        this.mp3Model = mp3Model;
        menuBarModel = mp3Model.getMenuBarModel();
        interactor = new MenuBarInteractor(mp3Model);
        viewBuilder = new MenuBarViewBuilder(
                menuBarModel,
                interactor::selectTheme,
                interactor::setSelectedLocale,
                interactor::setSize,
                () -> interactor.openDirectory(loadSongs),
                interactor::changeLaunchMaximized,
                interactor::changeDetailsSideMenuEnabled,
                interactor::changeStatusBarEnabled,
                () -> System.exit(0),
                interactor::setTracksForSongsByFileName,
                interactor::removeIndexFromFileNames,
                this::onSave,
                this::onSaveAs
        );
        setupModel();
    }

    private void onSave() {
        saveSongs(Paths.get(mp3Model.getSelectedPath()));
    }

    private void onSaveAs() {
        Path saveLocation = interactor.getSaveLocation();
        if (saveLocation == null) return;
        saveSongs(saveLocation);
    }

    private void saveSongs(Path savePath) {
        SongSaver songSaver = new SongSaverImpl(Paths.get(mp3Model.getSelectedPath()), savePath, mp3Model.getSongs());
        songSaver.save();
    }

    private void setupModel() {
        Preferences preferences = PreferenceStoreImpl.getInstance().getPreferences();
        menuBarModel.getSelectedLocale().set(preferences.getSelectedLocale());
        menuBarModel.getSelectedTheme().set(preferences.getSelectedTheme());
        menuBarModel.getSelectedSize().set(preferences.getSelectedSize());
        menuBarModel.getLaunchMaximized().set(preferences.isLaunchMaximized());
        menuBarModel.getDetailsMenuEnabled().set(preferences.isSideMenuEnabled());
        menuBarModel.getStatusBarEnabled().set(preferences.isStatusBarEnabled());
    }
}
