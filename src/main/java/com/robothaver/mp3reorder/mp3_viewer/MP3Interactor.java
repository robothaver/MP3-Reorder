package com.robothaver.mp3reorder.mp3_viewer;

import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import com.robothaver.mp3reorder.mp3_viewer.song.track.editor.MP3TrackEditor;
import com.robothaver.mp3reorder.mp3_viewer.song.track.editor.MP3TrackEditorImpl;
import javafx.scene.control.Alert;

public class MP3Interactor {
    private final MP3Model model;
    private final MP3TrackEditor mp3TrackEditor;

    public MP3Interactor(MP3Model model) {
        this.model = model;
        this.mp3TrackEditor = new MP3TrackEditorImpl(model);
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
    }

    public void onTrackChangedForSong(int currentTrack, int newTrack) {
        mp3TrackEditor.setNewTrackForSong(currentTrack, newTrack);
    }
}
