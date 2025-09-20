package com.robothaver.mp3reorder.mp3_viewer.controls.menubar;

import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class MenuBarController {
    private final MP3Model mp3Model;
    private final MenuBarModel model;
    private final Builder<MenuBar> viewBuilder;
    private final MenuBarInteractor interactor;

    public MenuBarController(MP3Model mp3Model) {
        this.mp3Model = mp3Model;
        this.model = new MenuBarModel();
        interactor = new MenuBarInteractor(model);
        viewBuilder = new MenuBarViewBuilder(
                model,
                interactor::selectTheme,
                interactor::setSize
        );
    }

    public MenuBar getView() {
        return viewBuilder.build();
    }
}
