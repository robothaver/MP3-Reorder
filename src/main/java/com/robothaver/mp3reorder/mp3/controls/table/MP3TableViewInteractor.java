package com.robothaver.mp3reorder.mp3.controls.table;

import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.mp3.controls.search.SearchTextFieldModel;
import com.robothaver.mp3reorder.mp3.domain.Song;
import com.robothaver.mp3reorder.mp3.song.track.editor.MP3TrackEditor;
import javafx.scene.control.Alert;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.awt.*;

@Log4j2
@RequiredArgsConstructor
public class MP3TableViewInteractor {
    private final MP3TableViewModel model;
    private final MP3TrackEditor mp3TrackEditor;
    private final SearchTextFieldModel searchModel;

    private final Desktop desktop = getDesktop();
    private final ViewLocalization localization = new ViewLocalization("language.table", LanguageController.getSelectedLocale());

    public void onFileRenamed(String oldName, String newName) {
        Song selectedSong = model.getSongs().get(model.getSelectedIndex());
        for (Song song : model.getSongs()) {
            if (!song.equals(selectedSong) && song.getFileName().equals(newName)) {
                selectedSong.fileNameProperty().set(oldName);
                DialogManagerImpl.getInstance().showAlert(Alert.AlertType.WARNING, localization.getForKey("rename.failed.title"), localization.getForKey("rename.failed"));
                return;
            }
        }
        searchModel.clear();
    }

    public void onSongDragged(int originalIndex, int newIndex) {
        model.setHoveredIndex(newIndex);
        mp3TrackEditor.insertSong(originalIndex, newIndex);
        searchModel.clear();
    }

    public void onTrackChangedForSong(int currentTrack, int newTrack) {
        mp3TrackEditor.setNewTrackForSong(currentTrack, newTrack);
    }

    public void revealInFolder() {
        if (desktop == null) {
            showActionDesktopError(localization.getForKey("action.reveal_in_folder"));
            return;
        }

        Song selectedSong = getSelectedSong();
        if (selectedSong == null) return;

        try {
            desktop.browseFileDirectory(selectedSong.getPath().toFile());
        } catch (Exception e) {
            log.error("Failed to reveal song in folder", e);
            showActionError(localization.getForKey("action.reveal_in_folder"), e);
        }
    }

    public void openInDefaultPlayer() {
        if (desktop == null) {
            showActionDesktopError(localization.getForKey("action.open_in_default_player"));
            return;
        }

        Song selectedSong = getSelectedSong();
        if (selectedSong == null) return;

        try {
            desktop.open(selectedSong.getPath().toFile());
        } catch (Exception e) {
            log.error("Failed to open song in default player", e);
            showActionError(localization.getForKey("action.open_in_default_player"), e);
        }
    }

    private Song getSelectedSong() {
        int selectedIndex = model.getSelectedIndex();
        if (selectedIndex == -1) return null;
        return model.getSongs().get(selectedIndex);
    }

    private void showActionError(String actionName, Exception exception) {
        showErrorDialog(localization.getForKey("action.failed.title"), "%s %s".formatted(localization.getForKey("action.failed").formatted(actionName), exception));
    }

    private void showActionDesktopError(String actionName) {
        showErrorDialog(localization.getForKey("action.unavailable"), localization.getForKey("desktop_error_message").formatted(actionName));
    }

    private void showErrorDialog(String title, String message) {
        DialogManagerImpl.getInstance().showAlert(Alert.AlertType.WARNING, title, message);
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
