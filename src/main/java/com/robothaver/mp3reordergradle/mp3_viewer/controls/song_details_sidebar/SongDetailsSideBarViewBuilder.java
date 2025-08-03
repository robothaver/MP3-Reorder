package com.robothaver.mp3reordergradle.mp3_viewer.controls.song_details_sidebar;

import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import com.robothaver.mp3reordergradle.mp3_viewer.MP3Model;
import com.robothaver.mp3reordergradle.mp3_viewer.Song;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SongDetailsSideBarViewBuilder implements Builder<Region> {
    private final MP3Model model;

    @Override
    public Region build() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPadding(new Insets(10));
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxWidth(800);
        VBox sideContainer = new VBox();
        sideContainer.setSpacing(10);
        sideContainer.setMaxWidth(Double.MAX_VALUE);

        Label songDetails = new Label("Song details");
        songDetails.getStyleClass().add(Styles.TITLE_3);
        songDetails.setMaxWidth(Double.MAX_VALUE);
        songDetails.setTextAlignment(TextAlignment.CENTER);

        Spacer spacer = new Spacer(Orientation.VERTICAL);

        SongTextDataWidget titleTextField = new SongTextDataWidget("Title");
        SongTextDataWidget artistTextField = new SongTextDataWidget("Artist");
        SongTextDataWidget albumTextField = new SongTextDataWidget("Album");
        SongTextDataWidget yearTextField = new SongTextDataWidget("Year");
        SongTextDataWidget genreDescriptionTextField = new SongTextDataWidget("Genre description");
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
        SongIntDataWidget genreSpinner = new SongIntDataWidget("Genre", 0);
        SongAlbumImageWidget songAlbumImageWidget = new SongAlbumImageWidget();

        scrollPane.widthProperty().addListener((observable, oldValue, newValue) ->
                songAlbumImageWidget.resizeImage((double) newValue)
        );


        sideContainer.getChildren().addAll(
                songDetails,
                spacer,
                songAlbumImageWidget.build(),
                titleTextField.build(),
                trackSpinner.build(),
                artistTextField.build(),
                albumTextField.build(),
                yearTextField.build(),
                genreSpinner.build(),
                genreDescriptionTextField.build(),
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
        scrollPane.setContent(sideContainer);

        model.selectedSongIndexProperty().addListener((observableValue, oldIndex, currentIndex) -> {
            Integer selectedIndex = (Integer) currentIndex;
            if (selectedIndex != -1) {
                Song song = model.getSongs().get(selectedIndex);
                songAlbumImageWidget.setImage(song.getAlbumImage());
                songAlbumImageWidget.resizeImage(scrollPane.widthProperty().get());

                genreSpinner.bind(song.genreProperty());
                trackSpinner.bind(song.trackProperty());

                titleTextField.bind(song.titleProperty());
                artistTextField.bind(song.artistProperty());
                albumTextField.bind(song.albumProperty());
                yearTextField.bind(song.yearProperty());
                genreDescriptionTextField.bind(song.genreDescriptionProperty());
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
        return scrollPane;
    }
}
