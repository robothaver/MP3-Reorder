package com.robothaver.mp3reorder.mp3.controls.details.controls.genre;

import com.mpatric.mp3agic.ID3v1Genres;
import com.robothaver.mp3reorder.mp3.controls.details.controls.SongComboBox;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SongGenreComboBox extends SongComboBox<SongGenreOption> {
    private IntegerProperty genreIndex;
    private StringProperty genreDescription;
    private boolean bindingChanged = false;

    public SongGenreComboBox(String title) {
        super(title);
        setOptions();
        setOnSelectionChanged();
    }

    public void bindToProperties(IntegerProperty genreIndexProperty, StringProperty genreDescriptionProperty) {
        genreIndex = genreIndexProperty;
        genreDescription = genreDescriptionProperty;
        selectGenre(genreIndexProperty.get(), genreDescriptionProperty.get());
    }

    private void setOptions() {
        ObservableList<SongGenreOption> genres = FXCollections.observableArrayList();
        for (int i = 0; i < ID3v1Genres.GENRES.length; i++) {
            genres.add(new SongGenreOption(i, ID3v1Genres.GENRES[i]));
        }
        getComboBox().setItems(genres);
    }

    private void setOnSelectionChanged() {
        getComboBox().valueProperty().addListener((_, _, newValue) -> {
            if (newValue != null && !bindingChanged) {
                genreIndex.setValue(newValue.getGenre());
                genreDescription.setValue(newValue.getGenreDescription());
            }
            bindingChanged = false;
        });
    }

    private void selectGenre(int genreIndex, String genreDescription) {
        boolean genreDescriptionValid = ID3v1Genres.matchGenreDescription(genreDescription) != -1;
        if (genreDescriptionValid) {
            bindingChanged = true;
            for (int i = 0; i < ID3v1Genres.GENRES.length; i++) {
                if (ID3v1Genres.GENRES[i].equals(genreDescription)) {
                    getComboBox().getSelectionModel().select(i);
                    break;
                }
            }
        } else if (genreIndex >= 0 && genreIndex < ID3v1Genres.GENRES.length) {
            bindingChanged = true;
            getComboBox().getSelectionModel().select(genreIndex);
        } else if (genreDescription.equals("null")) {
            if (getComboBox().getValue() != null) {
                // The previous value is not null
                bindingChanged = true;
            }
            getComboBox().setValue(null);
        } else {
            bindingChanged = true;
            getComboBox().getSelectionModel().select(12);
        }
    }
}
