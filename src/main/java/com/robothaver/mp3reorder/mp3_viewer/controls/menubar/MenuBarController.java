package com.robothaver.mp3reorder.mp3_viewer.controls.menubar;

import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.mp3_viewer.song.saver.SongSaver;
import com.robothaver.mp3reorder.mp3_viewer.song.saver.SongSaverImpl;
import javafx.scene.control.MenuBar;
import javafx.util.Builder;

public class MenuBarController {
    private final MenuBarModel model;
    private final Builder<MenuBar> viewBuilder;
    private final MenuBarInteractor interactor;

    public MenuBarController(MP3Model mp3Model, Runnable loadSongs) {
        this.model = new MenuBarModel();
        interactor = new MenuBarInteractor(model, mp3Model);
        viewBuilder = new MenuBarViewBuilder(
                model,
                mp3Model.getDetailsMenuEnabled(),
                interactor::selectTheme,
                interactor::setSize,
                () -> interactor.openDirectory(loadSongs),
                () -> System.exit(0),
                interactor::setTracksForSongsByFileName,
                interactor::removeIndexFromFileNames,
                () -> {
                    SongSaver songSaver = new SongSaverImpl();
                    songSaver.SaveSongs(mp3Model.getSongs());
                }
        );
    }

    public MenuBar getView() {
        return viewBuilder.build();
    }
}
