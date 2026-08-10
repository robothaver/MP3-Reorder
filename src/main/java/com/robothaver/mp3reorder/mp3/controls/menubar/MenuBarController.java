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
    private final Runnable onLoadSongs;

    private boolean reopenFolder;

    public MenuBarController(MP3Model mp3Model, Runnable onLoadSongs) {
        this.mp3Model = mp3Model;
        this.onLoadSongs = onLoadSongs;
        this.menuBarModel = mp3Model.getMenuBarModel();
        this.interactor = new MenuBarInteractor(mp3Model);
        this.viewBuilder = new MenuBarViewBuilder(
                menuBarModel,
                interactor::selectTheme,
                interactor::setSelectedLocale,
                interactor::setSize,
                () -> interactor.openDirectory(onLoadSongs),
                interactor::changeLaunchMaximized,
                interactor::changeUseSystemMenubar,
                interactor::changeDetailsSideMenuEnabled,
                interactor::changeStatusBarEnabled,
                interactor::onToggleAudioPlayer,
                () -> System.exit(0),
                interactor::setTracksForSongsByFileName,
                interactor::removeIndexFromFileNames,
                this::onSave,
                this::onSaveAs,
                this::onReopenFolder
        );
        setupModel();
    }

    private void onReopenFolder() {
        reopenFolder = true;
        if (interactor.hasUnsavedChanges() && interactor.shouldSaveChanges()) {
            onSave();
        } else {
            onLoadSongs.run();
        }
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
        SongSaver songSaver = new SongSaverImpl(Paths.get(mp3Model.getSelectedPath()), savePath, mp3Model.getSongs(), this::onSavingFinished);
        songSaver.save();
    }

    private void onSavingFinished() {
        if (reopenFolder) onLoadSongs.run();
        reopenFolder = false;
    }

    private void setupModel() {
        menuBarModel.getAudioPlayerEnabled().bindBidirectional(mp3Model.audioPlayerEnabledProperty());

        Preferences preferences = PreferenceStoreImpl.getInstance().getPreferences();
        menuBarModel.getSelectedLocale().set(preferences.getSelectedLocale());
        menuBarModel.getSelectedTheme().set(preferences.getSelectedTheme());
        menuBarModel.getSelectedSize().set(preferences.getSelectedSize());
        menuBarModel.getLaunchMaximized().set(preferences.isLaunchMaximized());
        menuBarModel.getUseSystemMenuBar().set(preferences.isUseSystemMenuBar());
        menuBarModel.getDetailsMenuEnabled().set(preferences.isSideMenuEnabled());
        menuBarModel.getStatusBarEnabled().set(preferences.isStatusBarEnabled());
        menuBarModel.getAudioPlayerEnabled().set(preferences.isAudioPlayerEnabled());
    }
}
