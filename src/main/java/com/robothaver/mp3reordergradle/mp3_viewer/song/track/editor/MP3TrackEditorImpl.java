package com.robothaver.mp3reordergradle.mp3_viewer.song.track.editor;

import com.robothaver.mp3reordergradle.mp3_viewer.MP3Model;
import com.robothaver.mp3reordergradle.mp3_viewer.song.domain.Song;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;

import static com.robothaver.mp3reordergradle.mp3_viewer.utils.Utils.TRACK_CONFLICT_MESSAGE;

@RequiredArgsConstructor
public class MP3TrackEditorImpl implements MP3TrackEditor {
    private final MP3Model model;
    @Override
    public void setNewTrackForSong(int currentTrack, int newTrack) {
        ObservableList<Song> songs = model.getSongs();
        for (int i = 0; i < songs.size(); i++) {
            if (i != model.getSelectedSongIndex() && songs.get(i).getTrack() == newTrack) {
                showTrackConflictMessage(currentTrack, newTrack);
                break;
            }
        }
    }

    private void showTrackConflictMessage(int currentTrack, int newTrack) {
        ButtonType insertButton = new ButtonType(TrackConflictSolutions.INSERT.getDisplayName(), ButtonBar.ButtonData.RIGHT);
        ButtonType putButton = new ButtonType(TrackConflictSolutions.PUT.getDisplayName(), ButtonBar.ButtonData.RIGHT);
        ButtonType cancelButton = new ButtonType(TrackConflictSolutions.CANCEL.getDisplayName(), ButtonBar.ButtonData.CANCEL_CLOSE);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().setStyle("-fx-padding: 0px 0px 0px 10px");
        dialog.setTitle("Track Conflict Detected");
        dialog.setContentText(TRACK_CONFLICT_MESSAGE);
        dialog.getDialogPane().getButtonTypes().addAll(cancelButton, putButton, insertButton);

        dialog.showAndWait()
                .ifPresent(buttonType -> {
                    TrackConflictSolutions conflictSolutions = TrackConflictSolutions.fromDisplayName(buttonType.getText());
                    handleTrackConflict(currentTrack, newTrack, conflictSolutions);
                });
    }

    private void handleTrackConflict(int currentTrack, int newTrack, TrackConflictSolutions solution) {
        ObservableList<Song> songs = model.getSongs();
        Song selectedSong = songs.get(model.getSelectedSongIndex());
        switch (solution) {
            case INSERT -> {
                songs.sort(Comparator.comparingInt(Song::getTrack));
                for (int i = 0; i < songs.size(); i++) {
                    if (songs.get(i).getTrack() == selectedSong.getTrack() && !songs.get(i).equals(selectedSong) && i != songs.size() - 1) {
                        setNewIndexForSong(i, i + 1);
                        songs.get(i + 1).setTrack(i + 2);
                        for (int j = i + 2; j < songs.size(); j++) {
                            if (j != songs.size() - 1) {
                                songs.get(j).setTrack(j + 1);
                            }
                        }
                        break;
                    }
                    // Todo fix not being able to update last element
                }
            }
            case PUT -> {

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
