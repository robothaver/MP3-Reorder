package com.robothaver.mp3reorder.mp3_viewer.controls.table;

import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.mp3_viewer.domain.Song;
import com.robothaver.mp3reorder.mp3_viewer.song.track.editor.MP3TrackEditor;
import com.robothaver.mp3reorder.mp3_viewer.song.track.editor.MP3TrackEditorImpl;
import javafx.scene.control.Alert;

import java.util.function.Consumer;

public class MP3TableViewInteractor {
    private final MP3Model model;
    private final MP3TrackEditor mp3TrackEditor;
    private final Consumer<Integer> onSelectedIndexChanged;

    public MP3TableViewInteractor(MP3Model model, Consumer<Integer> onSelectedIndexChanged) {
        this.model = model;
        this.mp3TrackEditor = new MP3TrackEditorImpl(model);
        this.onSelectedIndexChanged = onSelectedIndexChanged;
    }

    public void onFileRenamed(String oldName, String newName) {
        Song selectedSong = model.getSongs().get(model.getSelectedSongIndex());
        for (Song song : model.getSongs()) {
            if (!song.equals(selectedSong) && song.getFileName().equals(newName)) {
                selectedSong.fileNameProperty().set(oldName);
                DialogManagerImpl.getInstance().showAlert(Alert.AlertType.WARNING, "Can't rename file", "Can't have files with the same name!");
                return;
            }
        }
        model.getSongSearch().clear();
    }

    public void onTrackChangedForSong(int currentTrack, int newTrack) {
        mp3TrackEditor.setNewTrackForSong(currentTrack, newTrack);
        onSelectedIndexChanged.accept(model.getSelectedSongIndex());
    }
}
