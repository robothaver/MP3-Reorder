package com.robothaver.mp3reorder.mp3.controls.toolbar;

import com.robothaver.mp3reorder.core.BaseController;
import com.robothaver.mp3reorder.mp3.MP3Model;
import javafx.scene.control.ToolBar;

public class ToolBarController extends BaseController<ToolBar> {

    public ToolBarController(MP3Model model) {
        ToolBarInteractor interactor = new ToolBarInteractor(model);
        this.viewBuilder = new ToolBarViewBuilder(
                model,
                interactor::moveSelectedSongToTop,
                interactor::moveSelectedSongUp,
                interactor::moveSelectedSongDown,
                interactor::moveSelectedSongToBottom
        );
    }
}
