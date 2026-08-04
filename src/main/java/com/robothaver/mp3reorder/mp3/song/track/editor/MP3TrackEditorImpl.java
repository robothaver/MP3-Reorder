package com.robothaver.mp3reorder.mp3.song.track.editor;

import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.dialog.option.OptionDialogMessage;
import com.robothaver.mp3reorder.mp3.domain.Song;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MP3TrackEditorImpl implements MP3TrackEditor {
    private final ViewLocalization localization = new ViewLocalization("language.track_editor", LanguageController.getSelectedLocale());
    private final IntegerProperty selectedSongIndexProperty;
    private final BooleanProperty orderDescendingProperty;
    private final ObservableList<Song> songs;

    private Integer selectedSongCurrentTrack;


    @Override
    public void setNewTrackForSong(int currentTrack, int newTrack) {
        selectedSongCurrentTrack = currentTrack;

        Song conflictingSong = tryGetConflictingSong(newTrack);
        Song selectedSong = songs.get(selectedSongIndexProperty.get());
        if (conflictingSong != null) {
            // There is a song with the same track
            TrackConflictSolutions solution = getTrackConflictSolution();
            handleTrackConflict(solution, selectedSong, conflictingSong);
        } else {
            // Insert song with the new track in the valid range
            insertSongInTrackRange(newTrack, selectedSong);
        }
    }

    @Override
    public void insertSong(int currentSongIndex, int newSongIndex) {
        Song selectedSong = songs.get(currentSongIndex);
        Song nextSong = songs.get(newSongIndex);
        insertSongInternal(selectedSong, nextSong);
    }

    @Override
    public void swapSongsAndTracks(int index1, int index2) {
        Song song1 = songs.get(index1);
        Song song2 = songs.get(index2);

        int song1Track = song1.getTrack();
        song1.trackProperty().setValue(song2.getTrack());
        song2.trackProperty().setValue(song1Track);

        songs.set(index1, song2);
        songs.set(index2, song1);
    }

    private void insertSongInTrackRange(int newTrack, Song selectedSong) {
        // Keeps the tracks in valid range
        if (newTrack < 1) {
            insertSongInternal(selectedSong, songs.getFirst());
        } else if (newTrack > songs.size()) {
            insertSongInternal(selectedSong, songs.getLast());
        }
    }

    private Song tryGetConflictingSong(int track) {
        for (int i = 0; i < songs.size(); i++) {
            if (i != selectedSongIndexProperty.get() && songs.get(i).getTrack() == track) {
                return songs.get(i);
            }
        }
        return null;
    }

    private TrackConflictSolutions getTrackConflictSolution() {
        ButtonType insertButton = new ButtonType(localization.getForKey("insert"), ButtonBar.ButtonData.RIGHT);
        ButtonType switchButton = new ButtonType(localization.getForKey("switch"), ButtonBar.ButtonData.RIGHT);
        ButtonType cancelButton = new ButtonType(localization.getForKey("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);

        OptionDialogMessage trackConflictDetected = OptionDialogMessage.builder()
                .title(localization.getForKey("title"))
                .message(localization.getForKey("message"))
                .options(List.of(cancelButton, switchButton, insertButton))
                .build();

        ButtonType result = DialogManagerImpl.getInstance()
                .showOptionDialog(trackConflictDetected)
                .orElse(null);

        if (result == null) return TrackConflictSolutions.CANCEL;
        else if (result.equals(insertButton)) return TrackConflictSolutions.INSERT;
        else if (result.equals(switchButton)) return TrackConflictSolutions.SWITCH;
        else return TrackConflictSolutions.CANCEL;
    }

    private void handleTrackConflict(TrackConflictSolutions solution, Song selectedSong, Song conflictingSong) {
        switch (solution) {
            case INSERT -> insertSongInternal(selectedSong, conflictingSong);
            case SWITCH -> switchTracksForSongs(selectedSong, conflictingSong);
            default -> selectedSong.trackProperty().setValue(selectedSongCurrentTrack);
        }
    }

    private void insertSongInternal(Song selectedSong, Song conflictingSong) {
        if (songs.size() < 2) return;

        int conflictingSongIndex = songs.indexOf(conflictingSong);
        int selectedSongIndex = songs.indexOf(selectedSong);
        if (conflictingSongIndex == -1 || selectedSongIndex == -1) return;

        songs.remove(selectedSong);
        songs.add(conflictingSongIndex, selectedSong);

        int smallerIndex = Math.min(selectedSongIndex, conflictingSongIndex);
        int largerIndex = Math.max(selectedSongIndex, conflictingSongIndex);

        for (int i = smallerIndex; i <= largerIndex; i++) {
            int track = orderDescendingProperty.get() ? songs.size() - i : i + 1;
            if (track != songs.get(i).getTrack()) {
                songs.get(i).trackProperty().set(track);
            }
        }
        selectedSongIndexProperty.setValue(conflictingSongIndex);
    }

    private void switchTracksForSongs(Song selectedSong, Song conflictingSong) {
        // Setting track to original value otherwise they would have the same track
        selectedSong.trackProperty().setValue(selectedSongCurrentTrack);
        int selectedSongIndex = songs.indexOf(selectedSong);
        int conflictingSongIndex = songs.indexOf(conflictingSong);
        swapSongsAndTracks(selectedSongIndex, conflictingSongIndex);
        selectedSongIndexProperty.set(conflictingSongIndex);
    }
}
