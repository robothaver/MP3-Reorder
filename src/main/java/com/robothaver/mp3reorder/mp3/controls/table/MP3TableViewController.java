package com.robothaver.mp3reorder.mp3.controls.table;

import com.robothaver.mp3reorder.core.BaseController;
import com.robothaver.mp3reorder.mp3.MP3Model;
import com.robothaver.mp3reorder.mp3.domain.Song;
import javafx.scene.control.TableView;

public class MP3TableViewController extends BaseController<TableView<Song>> {
    public MP3TableViewController(MP3Model model) {
        MP3TableViewInteractor interactor = new MP3TableViewInteractor(model);
        viewBuilder = new MP3TableViewBuilder(
                model.getSongs(),
                model.getOrderDescending(),
                model.selectedSongIndexProperty(),
                interactor::onTrackChangedForSong,
                interactor::onFileRenamed,
                interactor::onSongDragged
        );
    }
}
