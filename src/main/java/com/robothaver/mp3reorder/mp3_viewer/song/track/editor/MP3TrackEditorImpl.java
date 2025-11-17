package com.robothaver.mp3reorder.mp3_viewer.song.track.editor;

import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.dialog.option.OptionDialogMessage;
import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.mp3_viewer.domain.Song;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Comparator;
import java.util.List;

import static com.robothaver.mp3reorder.mp3_viewer.utils.Utils.TRACK_CONFLICT_MESSAGE;

public class MP3TrackEditorImpl implements MP3TrackEditor {
    private final MP3Model model;
    private final List<Song> songs;
    private Integer selectedSongCurrentTrack;

    public MP3TrackEditorImpl(MP3Model model) {
        this.model = model;
        this.songs = model.getSongs();
    }

    @Override
    public void setNewTrackForSong(int currentTrack, int newTrack) {
        selectedSongCurrentTrack = currentTrack;

        Song conflictingSong = tryGetConflictingSong(newTrack);
        Song selectedSong = songs.get(model.getSelectedSongIndex());
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
    public void swapSongsAndTracks(int index1, int index2) {
        Song song1 = songs.get(index1);
        Song song2 = songs.get(index2);

        int song1Track = song1.getTrack();
        song1.trackProperty().setValue(song2.getTrack());
        song2.trackProperty().setValue(song1Track);

        songs.set(index1, songs.get(index2));
        songs.set(index2, song1);
    }

    private void insertSongInTrackRange(int newTrack , Song selectedSong) {
        if (newTrack < 1) {
            insertSong(selectedSong, songs.getFirst());
        } else if (newTrack > songs.size()) {
            insertSong(selectedSong, songs.getLast());
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
                    .trackProperty().setValue(selectedSongCurrentTrack);
        }
    }

    private void insertSong(Song selectedSong, Song conflictingSong) {
        selectedSong.trackProperty().setValue(selectedSongCurrentTrack);
        songs.sort(Comparator.comparingInt(Song::getTrack));
        int selectedSongIndex = songs.indexOf(selectedSong);
        int conflictingSongIndex = songs.indexOf(conflictingSong);
        int positionDif = selectedSongIndex - conflictingSongIndex;

        // The selected song is to the right in the list
        if (positionDif > 0) {
            for (int i = selectedSongIndex; i > conflictingSongIndex; i--) {
                // Move selected songs to the left in the list
                swapSongsAndTracks(i, i - 1);
            }
        } else {
            for (int i = selectedSongIndex; i < conflictingSongIndex; i++) {
                // Move selected songs to the right the list
                swapSongsAndTracks(i, i + 1);
            }
        }
        model.selectedSongIndexProperty().setValue(conflictingSongIndex);
    }

    private void switchTracksForSongs(Song selectedSong, Song conflictingSong) {
        // Setting track to original value otherwise they would have the same track
        selectedSong.trackProperty().setValue(selectedSongCurrentTrack);
        int selectedSongIndex = songs.indexOf(selectedSong);
        int conflictingSongIndex = songs.indexOf(conflictingSong);
        swapSongsAndTracks(selectedSongIndex, conflictingSongIndex);
        model.selectedSongIndexProperty().set(conflictingSongIndex);
    }
}
