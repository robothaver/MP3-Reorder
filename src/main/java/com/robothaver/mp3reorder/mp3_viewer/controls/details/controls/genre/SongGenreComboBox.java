package com.robothaver.mp3reorder.mp3_viewer.controls.details.controls.genre;

import com.mpatric.mp3agic.ID3v1Genres;
import com.robothaver.mp3reorder.mp3_viewer.controls.details.controls.SongComboBoxWidget;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SongGenreComboBox extends SongComboBoxWidget<SongGenreOption> {
    private IntegerProperty genreIndex;
    private StringProperty genreDescription;
    private boolean bindingChanged;

    public SongGenreComboBox(String title) {
        super(title);
        setOptions();
        setOnSelectionChanged();
    }

    public void bindToProperties(IntegerProperty genreIndexProperty, StringProperty genreDescriptionProperty) {
        genreIndex = genreIndexProperty;
        genreDescription = genreDescriptionProperty;
        bindingChanged = true;
        getComboBox().setValue(new SongGenreOption(genreIndexProperty.get(), genreDescriptionProperty.get()));
    }

    private void setOptions() {
        ObservableList<SongGenreOption> genres = FXCollections.observableArrayList();
        for (int i = 0; i < ID3v1Genres.GENRES.length; i++) {
            genres.add(new SongGenreOption(i, ID3v1Genres.GENRES[i]));
        }
        getComboBox().setItems(genres);
    }

    private void setOnSelectionChanged() {
        getComboBox().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!bindingChanged && newValue != null) {
                genreIndex.setValue(newValue.getGenre());
                genreDescription.setValue(newValue.getGenreDescription());
            } else {
                bindingChanged = false;
            }
        });
    }
}
