package com.robothaver.mp3reorder.mp3_viewer.song.track.editor;

import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.dialog.OptionDialogMessage;
import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Comparator;
import java.util.List;

import static com.robothaver.mp3reorder.mp3_viewer.utils.Utils.TRACK_CONFLICT_MESSAGE;

public class MP3TrackEditorImpl implements MP3TrackEditor {
    private final MP3Model model;
    private final List<Song> songs;
    private Integer selectedSongCurrentTrack;
    private Integer selectedSongCurrentNewTrack;

    public MP3TrackEditorImpl(MP3Model model) {
        this.model = model;
        this.songs = model.getSongs();
    }

    @Override
    public void setNewTrackForSong(int currentTrack, int newTrack) {
        selectedSongCurrentTrack = currentTrack;
        selectedSongCurrentNewTrack = newTrack;

        Song conflictingSong = tryGetConflictingSong(newTrack);
        if (conflictingSong != null) {
            Song selectedSong = songs.get(model.getSelectedSongIndex());
            TrackConflictSolutions solution = getTrackConflictSolution();
            handleTrackConflict(solution, selectedSong, conflictingSong);
        }
    }

    private Song tryGetConflictingSong(int track) {
        for (int i = 0; i < songs.size(); i++) {
            if (i != model.getSelectedSongIndex() && songs.get(i).getTrack() == track) {
                return songs.get(i);
            }
        }
        return null;
    }

    private TrackConflictSolutions getTrackConflictSolution() {
        ButtonType insertButton = new ButtonType(TrackConflictSolutions.INSERT.getDisplayName(), ButtonBar.ButtonData.RIGHT);
        ButtonType putButton = new ButtonType(TrackConflictSolutions.SWITCH.getDisplayName(), ButtonBar.ButtonData.RIGHT);
        ButtonType cancelButton = new ButtonType(TrackConflictSolutions.CANCEL.getDisplayName(), ButtonBar.ButtonData.CANCEL_CLOSE);

        OptionDialogMessage trackConflictDetected = OptionDialogMessage.builder()
                .title("Track Conflict Detected")
                .message(TRACK_CONFLICT_MESSAGE)
                .options(List.of(cancelButton, putButton, insertButton))
                .build();

        ButtonType result = DialogManagerImpl.getInstance()
                .showOptionDialog(trackConflictDetected)
                .orElse(null);
        if (result == null) {
            return TrackConflictSolutions.CANCEL;
        } else {
            return TrackConflictSolutions.fromDisplayName(result.getText());
        }
    }

    private void handleTrackConflict(TrackConflictSolutions solution, Song selectedSong, Song conflictingSong) {
        switch (solution) {
            case INSERT -> insertSong(selectedSong, conflictingSong);
            case SWITCH -> switchTracksForSongs(selectedSong, conflictingSong);
            default -> songs
                    .get(model.getSelectedSongIndex())
                    .setTrack(selectedSongCurrentTrack);
        }
    }

    private void insertSong(Song selectedSong, Song conflictingSong) {
        selectedSong.setTrack(selectedSongCurrentTrack);
        songs.sort(Comparator.comparingInt(Song::getTrack));
        int selectedSongIndex = songs.indexOf(selectedSong);
        int conflictingSongIndex = songs.indexOf(conflictingSong);
        int positionDif = selectedSongIndex - conflictingSongIndex;

        // The selected song is to the right in the list
        if (positionDif > 0) {
            for (int i1 = conflictingSongIndex; i1 < selectedSongIndex; i1++) {
                // Move songs to the right in the list
                setNewIndexForSong(selectedSongIndex - i1, selectedSongIndex - i1 - 1);
            }
        } else {
            for (int i1 = selectedSongIndex; i1 < conflictingSongIndex; i1++) {
                // Move songs to the left in the list
                setNewIndexForSong(i1, (i1 + 1));
            }
        }
        model.selectedSongIndexProperty().set(conflictingSongIndex);
    }

    private void switchTracksForSongs(Song selectedSong, Song conflictingSong) {
        // Setting track to original value otherwise they would have the same track
        selectedSong.setTrack(selectedSongCurrentTrack);
        int conflictingSongIndex = songs.indexOf(conflictingSong);
        int selectedSongIndex = songs.indexOf(selectedSong);
        setNewIndexForSong(selectedSongIndex, conflictingSongIndex);
        model.selectedSongIndexProperty().set(conflictingSongIndex);
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
