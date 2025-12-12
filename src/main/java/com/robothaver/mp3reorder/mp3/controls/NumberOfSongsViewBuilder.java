package com.robothaver.mp3reorder.mp3.controls;

import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.mp3.domain.Song;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Builder;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

public class NumberOfSongsViewBuilder implements Builder<HBox> {
    private final ViewLocalization localization = new ViewLocalization("language.number_of_songs", LanguageController.getSelectedLocale());
    private final StringBinding binding;

    public NumberOfSongsViewBuilder(ObservableList<Song> songs) {
        binding = Bindings.createStringBinding(() -> String.valueOf(songs.size()), songs);
    }

    @Override
    public HBox build() {
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(3);
        Label numberOfSongsLabel = new Label("Songs", new FontIcon(Feather.MUSIC));
        numberOfSongsLabel.textProperty().bind(localization.bindString("songs"));

        Label songsNumberLabel = new Label();
        songsNumberLabel.textProperty().bind(binding);

        root.getChildren().addAll(numberOfSongsLabel, songsNumberLabel);
        return root;
    }
}
