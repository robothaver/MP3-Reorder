package com.robothaver.mp3reorder.mp3_viewer.controls.detailes;

import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.mp3_viewer.controls.detailes.controls.SongAlbumImageWidget;
import com.robothaver.mp3reorder.mp3_viewer.controls.detailes.controls.SongTextDataWidget;
import com.robothaver.mp3reorder.mp3_viewer.controls.detailes.controls.genre.SongGenreComboBox;
import com.robothaver.mp3reorder.mp3_viewer.domain.Song;
import com.robothaver.mp3reorder.mp3_viewer.song.TagUtils;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

@RequiredArgsConstructor
public class SongDetailsSideMenuViewBuilder implements Builder<VBox> {
    private final MP3Model model;
    private final ViewLocalization localization = new ViewLocalization("language.detailsmenu", LanguageController.getSelectedLocale());

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
        createContent(scrollPane, detailsContainer);

        scrollPane.setContent(detailsContainer);

        rootContainer.getChildren().addAll(
                createToolBar(),
                scrollPane
        );

        return rootContainer;
    }

    private void createContent(ScrollPane scrollPane, VBox detailsContainer) {
        SongTextDataWidget titleTextField = new SongTextDataWidget("Title");
        titleTextField.getTitleProperty().bind(localization.bindString("title"));
        SongTextDataWidget artistTextField = new SongTextDataWidget("Artist");
        artistTextField.getTitleProperty().bind(localization.bindString("artist"));
        SongTextDataWidget albumTextField = new SongTextDataWidget("Album");
        albumTextField.getTitleProperty().bind(localization.bindString("album"));
        SongTextDataWidget yearTextField = new SongTextDataWidget("Year");
        yearTextField.getTitleProperty().bind(localization.bindString("year"));
        SongGenreComboBox songGenreComboBox = new SongGenreComboBox("Genre");
        songGenreComboBox.getTitleProperty().bind(localization.bindString("genre"));
        SongTextDataWidget commentTextField = new SongTextDataWidget("Comment");
        commentTextField.getTitleProperty().bind(localization.bindString("comment"));
        SongTextDataWidget lyricsTextField = new SongTextDataWidget("Lyrics");
        lyricsTextField.getTitleProperty().bind(localization.bindString("lyrics"));
        SongTextDataWidget composerTextField = new SongTextDataWidget("Composer");
        composerTextField.getTitleProperty().bind(localization.bindString("composer"));
        SongTextDataWidget publisherTextField = new SongTextDataWidget("Publisher");
        publisherTextField.getTitleProperty().bind(localization.bindString("publisher"));
        SongTextDataWidget originalArtistTextField = new SongTextDataWidget("Original artist");
        originalArtistTextField.getTitleProperty().bind(localization.bindString("original.artist"));
        SongTextDataWidget albumArtistTextField = new SongTextDataWidget("Album artist");
        albumArtistTextField.getTitleProperty().bind(localization.bindString("album.artist"));
        SongTextDataWidget copyrightTextField = new SongTextDataWidget("Copyright");
        copyrightTextField.getTitleProperty().bind(localization.bindString("copyright"));
        SongTextDataWidget urlTextField = new SongTextDataWidget("Url");
        urlTextField.getTitleProperty().bind(localization.bindString("url"));
        SongTextDataWidget encoderTextField = new SongTextDataWidget("Encoder");
        encoderTextField.getTitleProperty().bind(localization.bindString("encoder"));
        SongTextDataWidget trackTextField = new SongTextDataWidget("Track");
        trackTextField.getTitleProperty().bind(localization.bindString("track"));
        SongAlbumImageWidget songAlbumImageWidget = new SongAlbumImageWidget();

        scrollPane.widthProperty().addListener((observable, oldValue, newValue) ->
                songAlbumImageWidget.resizeImage((double) newValue)
        );

        detailsContainer.getChildren().addAll(
                songAlbumImageWidget.build(),
                titleTextField.build(),
                trackTextField.build(),
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
        trackTextField.setEditable(false);

        model.selectedSongIndexProperty().addListener((observableValue, oldIndex, currentIndex) -> {
            int selectedIndex = (int) currentIndex;
            if (selectedIndex != -1) {
                Song song = model.getSongs().get(selectedIndex);
                TagUtils.readDataFromTag(song);
                songAlbumImageWidget.setImage(song.getAlbumImage());
                songAlbumImageWidget.resizeImage(scrollPane.widthProperty().get());
                songGenreComboBox.bindToProperties(song.genreProperty(), song.genreDescriptionProperty());

                trackTextField.getTextField().setText(String.valueOf(song.getTrack()));
                song.trackProperty().addListener((observableValue1, oldTrack, newTrack) ->
                        trackTextField.getTextField().setText(String.valueOf(newTrack))
                );
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
    }

    private ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar();
        toolBar.setMinHeight(50);
        toolBar.setStyle("""
                -fx-border-style: hidden hidden solid solid;
                -fx-border-color: -color-border-muted;
                """);
        toolBar.setPadding(new Insets(0, 10, 0, 10));

        Label songDetailsLabel = new Label();
        songDetailsLabel.textProperty().bind(localization.bindString("label"));
        songDetailsLabel.getStyleClass().add(Styles.TITLE_3);
        songDetailsLabel.setMaxWidth(Double.MAX_VALUE);
        songDetailsLabel.setTextAlignment(TextAlignment.CENTER);

        Button closeContainerButton = new Button(null, new FontIcon(Feather.X));
        closeContainerButton.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.FLAT);
        closeContainerButton.setOnAction(actionEvent -> model.getDetailsMenuEnabled().set(false));
        toolBar.getItems().addAll(songDetailsLabel, new Spacer(Orientation.HORIZONTAL), closeContainerButton);
        return toolBar;
    }
}
