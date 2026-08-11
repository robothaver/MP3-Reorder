package com.robothaver.mp3reorder.mp3.controls.details;

import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.mp3.MP3Model;
import com.robothaver.mp3reorder.mp3.controls.RoundedImageView;
import com.robothaver.mp3reorder.mp3.controls.details.controls.SongTextDataWidget;
import com.robothaver.mp3reorder.mp3.controls.details.controls.genre.SongGenreComboBox;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Builder;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

public class SongDetailsSideMenuViewBuilder implements Builder<VBox> {
    private final MP3Model mp3Model;
    private final ViewLocalization localization = new ViewLocalization("language.details_menu", LanguageController.getSelectedLocale());
    private final Runnable onClosePressed;

    private BooleanBinding emptyContainerVisible;
    private BooleanBinding scrollPaneVisible;

    public SongDetailsSideMenuViewBuilder(MP3Model mp3Model, Runnable onClosePressed) {
        this.mp3Model = mp3Model;
        this.onClosePressed = onClosePressed;
    }

    @Override
    public VBox build() {
        scrollPaneVisible = Bindings.createBooleanBinding(() -> mp3Model.getSelectedSongIndex() != -1, mp3Model.selectedSongIndexProperty());
        emptyContainerVisible = Bindings.createBooleanBinding(() -> mp3Model.getSelectedSongIndex() == -1, mp3Model.selectedSongIndexProperty());

        VBox rootContainer = new VBox();
        rootContainer.setMaxWidth(800);
        rootContainer.setPadding(new Insets(0));

        VBox detailsContainer = new VBox();
        detailsContainer.setSpacing(10);
        detailsContainer.setMaxWidth(Double.MAX_VALUE);

        ScrollPane scrollPane = createScrollPane();
        scrollPane.setContent(detailsContainer);

        createContent(scrollPane, detailsContainer);

        rootContainer.getChildren().addAll(
                createToolBar(),
                scrollPane,
                createEmptyContainer()
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

        StackPane imageViewContainer = new StackPane();
        imageViewContainer.setAlignment(Pos.CENTER);
        RoundedImageView songAlbumImageWidget = new RoundedImageView();
        scrollPane.widthProperty().addListener((_, _, newWidth) -> {
            double width = Math.clamp(newWidth.doubleValue(), 40, 370) - 30;
            songAlbumImageWidget.setFitWidth(width);
        });
        imageViewContainer.getChildren().add(songAlbumImageWidget);

        detailsContainer.getChildren().addAll(
                imageViewContainer,
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

        mp3Model.selectedSongProperty().addListener((_, _, song) -> {
            if (song == null) return;

            songAlbumImageWidget.setImageBytes(song.getAlbumImage());
            songGenreComboBox.bindToProperties(song.genreProperty(), song.genreDescriptionProperty());
            trackTextField.getTextField().setText(String.valueOf(song.getTrack()));
            song.trackProperty().addListener((_, _, newTrack) ->
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
        closeContainerButton.setOnAction(_ -> onClosePressed.run());
        toolBar.getItems().addAll(songDetailsLabel, new Spacer(Orientation.HORIZONTAL), closeContainerButton);

        return toolBar;
    }

    private ScrollPane createScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));
        scrollPane.hbarPolicyProperty().set(ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane.managedProperty().bind(scrollPaneVisible);
        scrollPane.visibleProperty().bind(scrollPaneVisible);
        return scrollPane;
    }

    private VBox createEmptyContainer() {
        VBox emptyContainer = new VBox();
        VBox.setVgrow(emptyContainer, Priority.ALWAYS);
        emptyContainer.setMaxWidth(Double.MAX_VALUE);
        emptyContainer.setAlignment(Pos.CENTER);

        Label noSongSelectedLabel = new Label();
        noSongSelectedLabel.getStyleClass().add(Styles.TITLE_4);
        noSongSelectedLabel.textProperty().bind(localization.bindString("emptyText"));
        emptyContainer.getChildren().add(noSongSelectedLabel);

        emptyContainer.managedProperty().bind(emptyContainerVisible);
        emptyContainer.visibleProperty().bind(emptyContainerVisible);
        return emptyContainer;
    }
}
