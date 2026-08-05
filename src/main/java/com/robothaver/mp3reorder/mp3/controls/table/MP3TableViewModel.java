package com.robothaver.mp3reorder.mp3.controls.table;

import com.robothaver.mp3reorder.mp3.domain.Song;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import lombok.RequiredArgsConstructor;

import java.util.function.IntConsumer;

@RequiredArgsConstructor
public class MP3TableViewModel {
    private final ObservableList<Song> songs;
    private final IntegerProperty selectedIndex = new SimpleIntegerProperty();
    private final IntegerProperty hoveredIndex = new SimpleIntegerProperty();
    private final BooleanProperty orderDescending = new SimpleBooleanProperty(false);
    private final BooleanProperty songPlaying = new SimpleBooleanProperty(false);
    private final BooleanProperty scrollToSelected = new SimpleBooleanProperty(false);
    private final ObjectProperty<Song> songInPlayer = new SimpleObjectProperty<>(null);

    private IntConsumer onTogglePlay;

    public ObservableList<Song> getSongs() {
        return songs;
    }

    public int getSelectedIndex() {
        return selectedIndex.get();
    }

    public IntegerProperty selectedIndexProperty() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex.set(selectedIndex);
    }

    public int getHoveredIndex() {
        return hoveredIndex.get();
    }

    public IntegerProperty hoveredIndexProperty() {
        return hoveredIndex;
    }

    public void setHoveredIndex(int hoveredIndex) {
        this.hoveredIndex.set(hoveredIndex);
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

    public boolean isSongPlaying() {
        return songPlaying.get();
    }

    public BooleanProperty songPlayingProperty() {
        return songPlaying;
    }

    public void setSongPlaying(boolean songPlaying) {
        this.songPlaying.set(songPlaying);
    }

    public boolean isScrollToSelected() {
        return scrollToSelected.get();
    }

    public BooleanProperty scrollToSelectedProperty() {
        return scrollToSelected;
    }

    public void setScrollToSelected(boolean scrollToSelected) {
        this.scrollToSelected.set(scrollToSelected);
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

    public IntConsumer getOnTogglePlay() {
        return onTogglePlay;
    }

    public void setOnTogglePlay(IntConsumer onTogglePlay) {
        this.onTogglePlay = onTogglePlay;
    }
}
