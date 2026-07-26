package com.robothaver.mp3reorder.mp3.controls.audioplayer;

import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import com.robothaver.mp3reorder.mp3.controls.RoundedImageView;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class AudioPlayerViewBuilder implements Builder<HBox> {
    private final AudioPlayerModel model;
    private final Runnable onPlaySong;

    @Override
    public HBox build() {
        HBox root = new HBox();
        HBox.setHgrow(root, Priority.ALWAYS);
        root.getStyleClass().add(Styles.BG_SUBTLE);

        Button playSong = new Button("Play song");
        playSong.setOnAction(_ -> onPlaySong.run());

        root.getChildren().addAll(createInfoHBox(), new Spacer(), playSong, new Spacer());

        return root;
    }

    private final Image defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/no_image.png")));


    private HBox createInfoHBox() {
        HBox root = new HBox();
        root.setStyle("-fx-background-color: red");
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(16));

        RoundedImageView imageView = new RoundedImageView();
        imageView.setImageFitWidth(50);
        model.getCoverImageBytes().addListener((_, _, newValue) -> {
            imageView.setImageBytes(newValue);
        });

        VBox textVBox = new VBox();
        textVBox.setAlignment(Pos.CENTER);

        Label songNameLabel = new Label();
        songNameLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            String songName = model.getSongName().get();
            return songName == null ? "No song selected" : songName;
        }, model.getSongName()));
        songNameLabel.getStyleClass().add(Styles.TEXT_BOLD);

        Label artistLabel = new Label();
        artistLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            String artist = model.getArtist().get();
            return artist == null ? "Artist unavailable" : artist;
        }, model.getArtist()));

        textVBox.getChildren().addAll(songNameLabel, artistLabel);
        root.getChildren().addAll(imageView, textVBox);

        return root;
    }
}
