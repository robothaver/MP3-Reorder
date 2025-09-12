package com.robothaver.mp3reordergradle.mp3_viewer.controls.table;

import com.robothaver.mp3reordergradle.mp3_viewer.Song;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

import java.util.function.Consumer;

public class TrackFieldTableCell extends TextFieldTableCell<Song, Integer> {
    private final Consumer<Integer> onTrackChanged;
    private Integer currentTrack;

    public TrackFieldTableCell(Consumer<Integer> onTrackChanged) {
        this.onTrackChanged = onTrackChanged;
        setConverter(createStringConverter());
    }

    @Override
    public void updateItem(Integer newTrack, boolean empty) {
        super.updateItem(newTrack, empty);
        if (newTrack != null && currentTrack != null && !newTrack.equals(currentTrack)) {
            onTrackChanged.accept(newTrack);
        }
        currentTrack = newTrack;
    }

    private StringConverter<Integer> createStringConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Integer integer) {
                return Integer.toString(integer);
            }

            @Override
            public Integer fromString(String s) {
                try {
                    return Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    return currentTrack;
                }
            }
        };
    }
}
