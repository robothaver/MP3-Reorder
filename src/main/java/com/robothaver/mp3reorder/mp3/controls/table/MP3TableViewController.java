package com.robothaver.mp3reorder.mp3.controls.table;

import com.robothaver.mp3reorder.core.BaseController;
import com.robothaver.mp3reorder.mp3.domain.Song;
import com.robothaver.mp3reorder.mp3.song.track.editor.MP3TrackEditor;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import lombok.Getter;

public class MP3TableViewController extends BaseController<TableView<Song>> {
    @Getter
    private final MP3TableViewModel model;

    public MP3TableViewController(MP3TrackEditor trackEditor, ObservableList<Song> songs) {
        model = new MP3TableViewModel(songs);
        MP3TableViewInteractor interactor = new MP3TableViewInteractor(model, trackEditor);
        viewBuilder = new MP3TableViewBuilder(
                model,
                interactor::onTrackChangedForSong,
                interactor::onFileRenamed,
                interactor::onSongDragged
        );
    }
}
