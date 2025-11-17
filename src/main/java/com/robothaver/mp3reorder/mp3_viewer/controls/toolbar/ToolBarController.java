package com.robothaver.mp3reorder.mp3_viewer.controls.toolbar;

import com.robothaver.mp3reorder.BaseController;
import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import javafx.scene.control.ToolBar;

import java.util.function.Consumer;

public class ToolBarController extends BaseController<ToolBar> {

    public ToolBarController(MP3Model model, Consumer<Integer> onSelectedIndexChanged) {
        ToolBarInteractor interactor = new ToolBarInteractor(model, onSelectedIndexChanged);
        this.viewBuilder = new ToolBarViewBuilder(
                model,
                interactor::moveSelectedSongUp,
                interactor::moveSelectedSongDown,
                onSelectedIndexChanged
        );
    }
}
