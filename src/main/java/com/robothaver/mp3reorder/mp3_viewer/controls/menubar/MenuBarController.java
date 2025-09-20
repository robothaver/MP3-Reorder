package com.robothaver.mp3reorder.mp3_viewer.controls.menubar;

import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import javafx.scene.control.MenuBar;
import javafx.util.Builder;

public class MenuBarController implements Builder<MenuBar> {
    private final MP3Model mp3Model;
    private final MenuBarModel model;
    private final Builder<MenuBar> viewBuilder;

    public MenuBarController(MP3Model mp3Model) {
        this.mp3Model = mp3Model;
        this.model = new MenuBarModel();
        MenuBarInteractor interactor = new MenuBarInteractor(model);
        viewBuilder = new MenuBarViewBuilder(
                model,
                interactor::selectTheme
        );
    }

    @Override
    public MenuBar build() {
        return viewBuilder.build();
    }
}
