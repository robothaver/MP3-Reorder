package com.robothaver.mp3reorder.mp3_viewer.controls.table;

import com.robothaver.mp3reorder.BaseController;
import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.mp3_viewer.domain.Song;
import javafx.scene.control.TableView;

import java.util.function.Consumer;

public class MP3TableViewController extends BaseController<TableView<Song>> {
    public MP3TableViewController(MP3Model model, Consumer<Integer> onSelectedIndexChanged) {
        MP3TableViewInteractor interactor = new MP3TableViewInteractor(model, onSelectedIndexChanged);
        viewBuilder = new MP3TableViewBuilder(
                model.getSongs(),
                interactor::onTrackChangedForSong,
                interactor::onFileRenamed
        );
    }
}
