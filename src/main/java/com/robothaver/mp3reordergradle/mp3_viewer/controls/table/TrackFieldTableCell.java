package com.robothaver.mp3reordergradle.mp3_viewer.controls.table;

import com.robothaver.mp3reordergradle.mp3_viewer.Song;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

import java.util.function.BiConsumer;

public class TrackFieldTableCell extends TextFieldTableCell<Song, Integer> {
    private final BiConsumer<Integer, Integer> onTrackChanged;
    private Integer currentTrack;
    private Integer newTrack;

    public TrackFieldTableCell(BiConsumer<Integer, Integer> onTrackChanged) {
        this.onTrackChanged = onTrackChanged;
        setConverter(createStringConverter());
    }

    @Override
    public void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        if (currentTrack == null) {
            currentTrack = item;
        } else {
            currentTrack = newTrack;
        }
        newTrack = item;
    }
    @Override
    public void commitEdit(Integer newValue) {
        super.commitEdit(newValue);
        if (!currentTrack.equals(newValue)) {
            onTrackChanged.accept(currentTrack, newValue);
        }
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
