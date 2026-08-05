package com.robothaver.mp3reorder.mp3.controls.table;

import com.robothaver.mp3reorder.mp3.domain.Song;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.TableRow;

import java.util.Objects;

public class SongTableRow extends TableRow<Song> {
    public SongTableRow(MP3TableViewModel model) {
        setOnMouseEntered(_ -> model.setHoveredIndex(getIndex()));
        setOnMouseExited(_ -> model.setHoveredIndex(-1));
        StringBinding styleBinding = Bindings.createStringBinding(() -> {
            if (Objects.equals(model.getSongInPlayer(), getItem())) {
                return "-fx-background-color: -color-accent-muted";
            }
            return "";
        }, model.songInPlayerProperty(), model.getSongs(), itemProperty());
        styleProperty().bind(styleBinding);
        setUserData(styleBinding);
    }
}
