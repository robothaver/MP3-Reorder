package com.robothaver.mp3reorder.mp3.controls.table;

import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.mp3.domain.Song;
import com.robothaver.mp3reorder.mp3.song.track.editor.MP3TrackEditor;
import javafx.scene.control.Alert;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class MP3TableViewInteractor {
    private final MP3TableViewModel model;
    private final MP3TrackEditor mp3TrackEditor;
    private final Desktop desktop = getDesktop();

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

    public void revealInFolder() {
        if (desktop == null) {
            showActionError("reveal song in folder");
            return;
        }

        Song selectedSong = getSelectedSong();
        if (selectedSong == null) return;

        try {
            desktop.browseFileDirectory(selectedSong.getPath().toFile());
        } catch (Exception e) {
            log.error("Failed to reveal song in folder", e);
            showErrorDialog("Action failed!", "Failed to reveal song in folder. " + e);
        }
    }

    public void openInDefaultPlayer() {
        if (desktop == null) {
            showActionError("open song in default player");
            return;
        }

        Song selectedSong = getSelectedSong();
        if (selectedSong == null) return;

        try {
            desktop.open(selectedSong.getPath().toFile());
        } catch (IOException e) {
            log.error("Failed to open song in default player", e);
            showErrorDialog("Action failed!", "Failed to open song in default player. " + e);
        }
    }

    public void playSelected() {
        Song selectedSong = getSelectedSong();
        if (selectedSong == null) return;

        if (model.getOnTogglePlay() == null) return;
        model.getOnTogglePlay().accept(model.getSongs().indexOf(selectedSong));
    }

    private Song getSelectedSong() {
        int selectedIndex = model.getSelectedIndex();
        if (selectedIndex == -1) return null;
        return model.getSongs().get(selectedIndex);
    }

    private void showActionError(String actionName) {
        showErrorDialog("Unavailable action", "Failed to %s because desktop could not be resolved!".formatted(actionName));
    }

    private void showErrorDialog(String title, String message) {
        DialogManagerImpl.getInstance()
                .showAlert(
                        Alert.AlertType.WARNING,
                        title,
                        message
                );
    }

    private Desktop getDesktop() {
        try {
            return Desktop.getDesktop();
        } catch (Exception e) {
            log.error("Failed to get desktop", e);
        }
        return null;
    }
}
