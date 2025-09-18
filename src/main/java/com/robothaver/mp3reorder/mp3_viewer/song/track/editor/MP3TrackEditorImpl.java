package com.robothaver.mp3reorder.mp3_viewer.song.track.editor;

import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import com.robothaver.mp3reorder.dialog.OptionDialogMessage;
import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;

import static com.robothaver.mp3reorder.mp3_viewer.utils.Utils.TRACK_CONFLICT_MESSAGE;

@RequiredArgsConstructor
public class MP3TrackEditorImpl implements MP3TrackEditor {
    private final MP3Model model;
    @Override
    public void setNewTrackForSong(int currentTrack, int newTrack) {
        ObservableList<Song> songs = model.getSongs();
        for (int i = 0; i < songs.size(); i++) {
            if (i != model.getSelectedSongIndex() && songs.get(i).getTrack() == newTrack) {
                showTrackConflictMessage(currentTrack, songs.get(i));
                break;
            }
        }
    }

    private void showTrackConflictMessage(int currentTrack, Song conflictingSong) {
        ButtonType insertButton = new ButtonType(TrackConflictSolutions.INSERT.getDisplayName(), ButtonBar.ButtonData.RIGHT);
        ButtonType putButton = new ButtonType(TrackConflictSolutions.SWITCH.getDisplayName(), ButtonBar.ButtonData.RIGHT);
        ButtonType cancelButton = new ButtonType(TrackConflictSolutions.CANCEL.getDisplayName(), ButtonBar.ButtonData.CANCEL_CLOSE);

        OptionDialogMessage trackConflictDetected = OptionDialogMessage.builder()
                .title("Track Conflict Detected")
                .message(TRACK_CONFLICT_MESSAGE)
                .options(List.of(cancelButton, putButton, insertButton))
                .build();

        DialogManagerImpl.getInstance()
                .showOptionDialog(trackConflictDetected)
                .ifPresent(buttonType -> {
                    TrackConflictSolutions conflictSolutions = TrackConflictSolutions.fromDisplayName(buttonType.getText());
                    handleTrackConflict(currentTrack, conflictSolutions, conflictingSong);
                });
    }

    private void handleTrackConflict(int currentTrack, TrackConflictSolutions solution, Song conflictingSong) {
        ObservableList<Song> songs = model.getSongs();
        Song selectedSong = songs.get(model.getSelectedSongIndex());
        selectedSong.setTrack(currentTrack);

        switch (solution) {
            case INSERT -> {
                songs.sort(Comparator.comparingInt(Song::getTrack));

                int selectedSongIndex = songs.indexOf(selectedSong);
                int conflictingSongIndex = songs.indexOf(conflictingSong);
                int positionDif = selectedSongIndex - conflictingSongIndex;

                // The selected song is further in the list
                if (positionDif > 0) {
                    for (int i1 = conflictingSongIndex; i1 < selectedSongIndex; i1++) {
                        // Move songs further in the list
                        setNewIndexForSong(selectedSongIndex - i1, selectedSongIndex - i1 - 1);
                    }
                } else {
                    for (int i1 = selectedSongIndex; i1 < conflictingSongIndex; i1++) {
                        // Move songs closer in the list
                        setNewIndexForSong(i1, (i1 + 1));
                    }
                }
                model.selectedSongIndexProperty().set(conflictingSongIndex);
            }
            case SWITCH -> {

            }
            default -> songs
                    .get(model.getSelectedSongIndex())
                    .setTrack(currentTrack);
        }
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
