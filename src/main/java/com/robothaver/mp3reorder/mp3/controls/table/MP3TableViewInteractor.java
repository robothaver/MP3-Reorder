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

@Log4j2
@RequiredArgsConstructor
public class MP3TableViewInteractor {
    private final MP3TableViewModel model;
    private final MP3TrackEditor mp3TrackEditor;
    private final SearchTextFieldModel searchModel;
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
        Song selectedSong = getSelectedSong();
        if (selectedSong == null) return;

        DesktopApi.revealInFolder(selectedSong.getPath());
    }

    public void openInDefaultPlayer() {
        Song selectedSong = getSelectedSong();
        if (selectedSong == null) return;

        DesktopApi.openInPlayer(selectedSong.getPath());
    }

    private Song getSelectedSong() {
        int selectedIndex = model.getSelectedIndex();
        if (selectedIndex == -1) return null;
        return model.getSongs().get(selectedIndex);
    }
}
