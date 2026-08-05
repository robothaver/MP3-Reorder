package com.robothaver.mp3reorder.mp3.controls.table;

import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.mp3.domain.Song;
import com.robothaver.mp3reorder.mp3.song.track.editor.MP3TrackEditor;
import javafx.scene.control.Alert;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MP3TableViewInteractor {
    private final MP3TableViewModel model;
    private final MP3TrackEditor mp3TrackEditor;

    public void onFileRenamed(String oldName, String newName) {
        Song selectedSong = model.getSongs().get(model.getSelectedIndex());
        for (Song song : model.getSongs()) {
            if (!song.equals(selectedSong) && song.getFileName().equals(newName)) {
                selectedSong.fileNameProperty().set(oldName);
                DialogManagerImpl.getInstance().showAlert(Alert.AlertType.WARNING, "Can't rename file", "Can't have files with the same name!");
                return;
            }
        }
        //mp3Model.getSongSearch().clear();
    }

    public void onSongDragged(int originalIndex, int newIndex) {
        model.setHoveredIndex(newIndex);
        mp3TrackEditor.insertSong(originalIndex, newIndex);
    }

    public void onTrackChangedForSong(int currentTrack, int newTrack) {
        mp3TrackEditor.setNewTrackForSong(currentTrack, newTrack);
    }
}
