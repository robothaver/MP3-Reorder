package com.robothaver.mp3reorder.mp3_viewer.controls.toolbar;

import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.mp3_viewer.domain.Song;
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

    public void moveSelectedSongToTop() {
        int selectedIndex = model.getSelectedSongIndex();

        if (selectedIndex > 0) {
            System.out.println(selectedIndex);
            mp3TrackEditor.insertSong(selectedIndex, 0);
            setSelectedIndex(0);
        }
    }

    public void moveSelectedSongUp() {
        int selectedIndex = model.getSelectedSongIndex();
        if (selectedIndex > 0) {
            mp3TrackEditor.swapSongsAndTracks(selectedIndex, selectedIndex - 1);
            setSelectedIndex(selectedIndex - 1);
        }
    }

    public void moveSelectedSongDown() {
        int selectedIndex = model.getSelectedSongIndex();
        ObservableList<Song> songs = model.getSongs();
        if (selectedIndex != songs.size() - 1 && selectedIndex != -1) {
            mp3TrackEditor.swapSongsAndTracks(selectedIndex, selectedIndex + 1);
            setSelectedIndex(selectedIndex + 1);
        }
    }

    public void moveSelectedSongToBottom() {
        int selectedIndex = model.getSelectedSongIndex();
        int lastIndex = model.getSongs().size() - 1;
        if (selectedIndex != -1 && selectedIndex != lastIndex) {
            mp3TrackEditor.insertSong(selectedIndex, lastIndex);
            setSelectedIndex(lastIndex);
        }
    }

    private void setSelectedIndex(int index) {
        model.selectedSongIndexProperty().set(index);
        onSelectedIndexChanged.accept(index);
    }
}
