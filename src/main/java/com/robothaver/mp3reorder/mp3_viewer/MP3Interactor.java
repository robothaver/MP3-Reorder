package com.robothaver.mp3reorder.mp3_viewer;

import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.SongSearch;
import com.robothaver.mp3reorder.mp3_viewer.song.track.editor.MP3TrackEditor;
import com.robothaver.mp3reorder.mp3_viewer.song.track.editor.MP3TrackEditorImpl;
import javafx.collections.ObservableList;

public class MP3Interactor {
    private final MP3Model model;
    private final MP3TrackEditor mp3TrackEditor;

    public MP3Interactor(MP3Model model) {
        this.model = model;
        this.mp3TrackEditor = new MP3TrackEditorImpl(model);
    }

    public void onSearchQueryChanged() {
        SongSearch songSearch = model.getSongSearch();
        String searchQuery = songSearch.getSearchQuery().get().trim();
        if (searchQuery.isBlank()) {
            songSearch.getFound().set(true);
            return;
        }

        boolean found = false;
        for (Song song : model.getSongs()) {
            if (song.getFileName().equalsIgnoreCase(searchQuery) || song.getFileName().toLowerCase().contains(searchQuery)) {
                model.selectedSongIndexProperty().set(model.getSongs().indexOf(song));
                found = true;
                break;
            }
        }
        songSearch.getFound().set(found);
    }

    public void onTrackChangedForSong(int currentTrack, int newTrack) {
        mp3TrackEditor.setNewTrackForSong(currentTrack, newTrack);
    }

    public void moveSelectedSongUp(int selectedIndex) {
        if (selectedIndex != 0 && selectedIndex != -1) {
            mp3TrackEditor.setNewIndexForSong(selectedIndex, selectedIndex - 1);
        }
    }

    public void moveSelectedSongDown(int selectedIndex) {
        ObservableList<Song> songs = model.getSongs();
        if (selectedIndex != songs.size() - 1 && selectedIndex != -1) {
            mp3TrackEditor.setNewIndexForSong(selectedIndex, selectedIndex + 1);
        }
    }
}
