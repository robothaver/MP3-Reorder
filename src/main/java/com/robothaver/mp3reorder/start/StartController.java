package com.robothaver.mp3reorder.start;

import com.robothaver.mp3reorder.core.BaseController;
import com.robothaver.mp3reorder.mp3.MP3Controller;
import javafx.scene.layout.StackPane;

public class StartController extends BaseController<StackPane> {
    public StartController() {
        StartModel startModel = new StartModel();
        MP3Controller mp3Controller = new MP3Controller();
        StartInteractor interactor = new StartInteractor(startModel, mp3Controller);
        viewBuilder = new StartViewBuilder(mp3Controller.getView(), startModel, interactor::chooseSongLocation);
    }
}
