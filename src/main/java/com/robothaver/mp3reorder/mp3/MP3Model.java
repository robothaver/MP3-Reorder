package com.robothaver.mp3reorder.mp3;

import com.robothaver.mp3reorder.mp3.controls.menubar.MenuBarModel;
import com.robothaver.mp3reorder.mp3.controls.search.SearchTextFieldModel;
import com.robothaver.mp3reorder.mp3.domain.Song;
import com.robothaver.mp3reorder.mp3.song.track.editor.MP3TrackEditor;
import com.robothaver.mp3reorder.mp3.song.track.editor.MP3TrackEditorImpl;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MP3Model {
    private final ObservableList<Song> songs = FXCollections.observableArrayList();
    private final BooleanProperty orderDescending = new SimpleBooleanProperty(false);
    private final StringProperty selectedPath = new SimpleStringProperty();
    private final IntegerProperty selectedSongIndex = new SimpleIntegerProperty(-1);
    private final ObjectProperty<Song> songInPlayer = new SimpleObjectProperty<>();
    private final BooleanProperty songPlaying = new SimpleBooleanProperty();
    private final SearchTextFieldModel songSearch = new SearchTextFieldModel();
    private final MenuBarModel menuBarModel = new MenuBarModel();
    private final MP3TrackEditor trackEditor = new MP3TrackEditorImpl(selectedSongIndex, orderDescending, songs);
    private final BooleanProperty scrollToSelected = new SimpleBooleanProperty(false);

    public ObservableList<Song> getSongs() {
        return songs;
    }

    public boolean isOrderDescending() {
        return orderDescending.get();
    }

    public BooleanProperty orderDescendingProperty() {
        return orderDescending;
    }

    public void setOrderDescending(boolean orderDescending) {
        this.orderDescending.set(orderDescending);
    }

    public String getSelectedPath() {
        return selectedPath.get();
    }

    public StringProperty selectedPathProperty() {
        return selectedPath;
    }

    public void setSelectedPath(String selectedPath) {
        this.selectedPath.set(selectedPath);
    }

    public int getSelectedSongIndex() {
        return selectedSongIndex.get();
    }

    public IntegerProperty selectedSongIndexProperty() {
        return selectedSongIndex;
    }

    public void setSelectedSongIndex(int selectedSongIndex) {
        this.selectedSongIndex.set(selectedSongIndex);
    }

    public Song getSongInPlayer() {
        return songInPlayer.get();
    }

    public ObjectProperty<Song> songInPlayerProperty() {
        return songInPlayer;
    }

    public void setSongInPlayer(Song songInPlayer) {
        this.songInPlayer.set(songInPlayer);
    }

    public boolean isSongPlaying() {
        return songPlaying.get();
    }

    public BooleanProperty songPlayingProperty() {
        return songPlaying;
    }

    public void setSongPlaying(boolean songPlaying) {
        this.songPlaying.set(songPlaying);
    }

    public SearchTextFieldModel getSongSearch() {
        return songSearch;
    }

    public MenuBarModel getMenuBarModel() {
        return menuBarModel;
    }

    public MP3TrackEditor getTrackEditor() {
        return trackEditor;
    }

    public boolean getScrollToSelected() {
        return scrollToSelected.get();
    }

    public BooleanProperty scrollToSelectedProperty() {
        return scrollToSelected;
    }

    public void setScrollToSelected(boolean scrollToSelected) {
        this.scrollToSelected.set(scrollToSelected);
    }
}
