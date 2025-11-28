package com.robothaver.mp3reorder.mp3_viewer.controls.detailes;

import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.mp3_viewer.controls.detailes.controls.SongAlbumImageWidget;
import com.robothaver.mp3reorder.mp3_viewer.controls.detailes.controls.SongIntDataWidget;
import com.robothaver.mp3reorder.mp3_viewer.controls.detailes.controls.SongTextDataWidget;
import com.robothaver.mp3reorder.mp3_viewer.controls.detailes.controls.genre.SongGenreComboBox;
import com.robothaver.mp3reorder.mp3_viewer.song.TagUtils;
import com.robothaver.mp3reorder.mp3_viewer.domain.Song;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

@RequiredArgsConstructor
public class SongDetailsSideMenuViewBuilder implements Builder<VBox> {
    private final MP3Model model;

    @Override
    public VBox build() {
        VBox rootContainer = new VBox();
        rootContainer.setMaxWidth(800);
        rootContainer.setPadding(new Insets(0));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        VBox detailsContainer = new VBox();
        detailsContainer.setSpacing(10);
        detailsContainer.setMaxWidth(Double.MAX_VALUE);

        ToolBar toolBar = new ToolBar();
        toolBar.setMinHeight(50);
        toolBar.setStyle("""
                -fx-border-style: hidden hidden solid solid;
                -fx-border-color: -color-border-muted;
                """);
        toolBar.setPadding(new Insets(0, 10, 0, 10));

        Label songDetailsLabel = new Label("Song details");
        songDetailsLabel.getStyleClass().add(Styles.TITLE_3);
        songDetailsLabel.setMaxWidth(Double.MAX_VALUE);
        songDetailsLabel.setTextAlignment(TextAlignment.CENTER);

        Button closeContainerButton = new Button(null, new FontIcon(Feather.X));
        closeContainerButton.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.FLAT);
        closeContainerButton.setOnAction(actionEvent -> model.getDetailsMenuEnabled().set(false));
        toolBar.getItems().addAll(songDetailsLabel, new Spacer(Orientation.HORIZONTAL), closeContainerButton);


        SongTextDataWidget titleTextField = new SongTextDataWidget("Title");
        SongTextDataWidget artistTextField = new SongTextDataWidget("Artist");
        SongTextDataWidget albumTextField = new SongTextDataWidget("Album");
        SongTextDataWidget yearTextField = new SongTextDataWidget("Year");
        SongGenreComboBox songGenreComboBox = new SongGenreComboBox("Genre");
        SongTextDataWidget commentTextField = new SongTextDataWidget("Comment");
        SongTextDataWidget lyricsTextField = new SongTextDataWidget("Lyrics");
        SongTextDataWidget composerTextField = new SongTextDataWidget("Composer");
        SongTextDataWidget publisherTextField = new SongTextDataWidget("Publisher");
        SongTextDataWidget originalArtistTextField = new SongTextDataWidget("Original artist");
        SongTextDataWidget albumArtistTextField = new SongTextDataWidget("Album artist");
        SongTextDataWidget copyrightTextField = new SongTextDataWidget("Copyright");
        SongTextDataWidget urlTextField = new SongTextDataWidget("Url");
        SongTextDataWidget encoderTextField = new SongTextDataWidget("Encoder");

        SongIntDataWidget trackSpinner = new SongIntDataWidget("Track", 1);
        SongAlbumImageWidget songAlbumImageWidget = new SongAlbumImageWidget();

        scrollPane.widthProperty().addListener((observable, oldValue, newValue) ->
                songAlbumImageWidget.resizeImage((double) newValue)
        );


        detailsContainer.getChildren().addAll(
                songAlbumImageWidget.build(),
                titleTextField.build(),
                trackSpinner.build(),
                artistTextField.build(),
                albumTextField.build(),
                yearTextField.build(),
                songGenreComboBox.build(),
                commentTextField.build(),
                lyricsTextField.build(),
                composerTextField.build(),
                publisherTextField.build(),
                originalArtistTextField.build(),
                albumArtistTextField.build(),
                copyrightTextField.build(),
                urlTextField.build(),
                encoderTextField.build()
        );
        scrollPane.setContent(detailsContainer);

        rootContainer.getChildren().addAll(toolBar, scrollPane);

        model.selectedSongIndexProperty().addListener((observableValue, oldIndex, currentIndex) -> {
            Integer selectedIndex = (Integer) currentIndex;
            if (selectedIndex != -1) {
                Song song = model.getSongs().get(selectedIndex);
                TagUtils.readDataFromTag(song);
                songAlbumImageWidget.setImage(song.getAlbumImage());
                songAlbumImageWidget.resizeImage(scrollPane.widthProperty().get());
                songGenreComboBox.bindToProperties(song.genreProperty(), song.genreDescriptionProperty());

                trackSpinner.bind(song.trackProperty());
                titleTextField.bind(song.titleProperty());
                artistTextField.bind(song.artistProperty());
                albumTextField.bind(song.albumProperty());
                yearTextField.bind(song.yearProperty());
                commentTextField.bind(song.commentProperty());
                lyricsTextField.bind(song.lyricsProperty());
                composerTextField.bind(song.composerProperty());
                publisherTextField.bind(song.publisherProperty());
                originalArtistTextField.bind(song.originalArtistProperty());
                albumArtistTextField.bind(song.albumArtistProperty());
                copyrightTextField.bind(song.copyrightProperty());
                urlTextField.bind(song.urlProperty());
                encoderTextField.bind(song.encoderProperty());
            }
        });
        return rootContainer;
    }
}
