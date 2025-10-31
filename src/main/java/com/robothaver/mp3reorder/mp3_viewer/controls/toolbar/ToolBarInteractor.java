package com.robothaver.mp3reorder.mp3_viewer.controls.toolbar;

import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.SongSearch;
import com.robothaver.mp3reorder.mp3_viewer.song.track.editor.MP3TrackEditor;
import com.robothaver.mp3reorder.mp3_viewer.song.track.editor.MP3TrackEditorImpl;
import javafx.collections.ObservableList;

import java.util.function.Consumer;

public class ToolBarInteractor {
    private final MP3Model model;
    private final Consumer<Integer> onSelectedIndexChanged;
    private final MP3TrackEditor mp3TrackEditor;

    public ToolBarInteractor(MP3Model model, Consumer<Integer> onSelectedIndexChanged) {
        this.model = model;
        this.mp3TrackEditor = new MP3TrackEditorImpl(model);
        this.onSelectedIndexChanged = onSelectedIndexChanged;
    }

    public void moveSelectedSongUp() {
        int selectedIndex = model.getSelectedSongIndex();
        if (selectedIndex > 0) {
            mp3TrackEditor.swapSongsAndTracks(selectedIndex, selectedIndex - 1);
            model.selectedSongIndexProperty().set(selectedIndex - 1);
            onSelectedIndexChanged.accept(selectedIndex - 1);
        }
    }

    public void moveSelectedSongDown() {
        int selectedIndex = model.getSelectedSongIndex();
        ObservableList<Song> songs = model.getSongs();
        if (selectedIndex != songs.size() - 1 && selectedIndex != -1) {
            mp3TrackEditor.swapSongsAndTracks(selectedIndex, selectedIndex + 1);
            model.selectedSongIndexProperty().set(selectedIndex + 1);
            onSelectedIndexChanged.accept(selectedIndex + 1);
        }
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
            if (song.getFileName().equalsIgnoreCase(searchQuery) || song.getFileName().toLowerCase().contains(searchQuery.toLowerCase())) {
                model.selectedSongIndexProperty().set(model.getSongs().indexOf(song));
                found = true;
                break;
            }
        }
        songSearch.getFound().set(found);
    }
}
