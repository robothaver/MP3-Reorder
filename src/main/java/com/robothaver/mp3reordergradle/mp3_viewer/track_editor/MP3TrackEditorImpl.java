package com.robothaver.mp3reordergradle.mp3_viewer.track_editor;

import com.robothaver.mp3reordergradle.mp3_viewer.MP3Model;
import com.robothaver.mp3reordergradle.mp3_viewer.Song;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.robothaver.mp3reordergradle.mp3_viewer.utils.Utils.TRACK_CONFLICT_MESSAGE;

@RequiredArgsConstructor
public class MP3TrackEditorImpl implements MP3TrackEditor {
    private final MP3Model model;
    @Override
    public void setNewTrackForSong(int currentTrack, int newTrack) {
        ObservableList<Song> songs = model.getSongs();
        for (int i = 0; i < songs.size(); i++) {
            if (i != model.getSelectedSongIndex().get() && songs.get(i).getTrack() == newTrack) {
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
        switch (solution) {
            case INSERT -> {

            }
            case PUT -> {

            }
            case CANCEL -> {
                model.getSongs().get(model.getSelectedSongIndex().get()).setTrack(currentTrack);
            }
        }
    }
}
