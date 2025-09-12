package com.robothaver.mp3reordergradle.mp3_viewer;

import com.robothaver.mp3reordergradle.mp3_viewer.track_editor.MP3TrackEditorImpl;
import com.robothaver.mp3reordergradle.mp3_viewer.utils.MP3FileUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MP3Interactor {
    private final MP3Model model;

    public void onTrackChangedForSong(int currentTrack, int newTrack) {
        MP3TrackEditorImpl mp3TrackEditor = new MP3TrackEditorImpl(model);
        mp3TrackEditor.setNewTrackForSong(currentTrack, newTrack);
    }

    public void setTracksForSongsByFileName() {
        ObservableList<Song> songs = model.getSongs();
        songs.sort((o1, o2) -> MP3FileUtils.compareFileNames(o1.getFileName(), o2.getFileName()));
        for (int i = 0; i < songs.size(); i++) {
            songs.get(i).setTrack(i + 1);
        }
    }

    public void moveSelectedSongUp(int selectedIndex) {
        if (selectedIndex != 0 && selectedIndex != -1) {
            setNewIndexForSong(selectedIndex, selectedIndex - 1);
        }
    }

    public void moveSelectedSongDown(int selectedIndex) {
        ObservableList<Song> songs = model.getSongs();
        if (selectedIndex != songs.size() - 1 && selectedIndex != -1) {
            setNewIndexForSong(selectedIndex, selectedIndex + 1);
        }
    }

    private void setNewIndexForSong(int selectedIndex, int newIndex) {
        ObservableList<Song> songs = model.getSongs();
        Song selectedSong = songs.get(selectedIndex);
        Song previousSong = songs.get(newIndex);

        int selectedSongTrack = selectedSong.getTrack();
        selectedSong.setTrack(previousSong.getTrack());
        previousSong.setTrack(selectedSongTrack);

        songs.set(selectedIndex, songs.get(newIndex));
        songs.set(newIndex, selectedSong);
    }
}
