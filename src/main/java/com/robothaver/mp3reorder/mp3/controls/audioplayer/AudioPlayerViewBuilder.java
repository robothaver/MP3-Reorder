package com.robothaver.mp3reorder.mp3.controls.audioplayer;

import atlantafx.base.controls.ProgressSliderSkin;
import atlantafx.base.theme.Styles;
import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.core.utils.ResourceHelper;
import com.robothaver.mp3reorder.mp3.controls.RoundedImageView;
import com.robothaver.mp3reorder.mp3.controls.ThemedIconButton;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class AudioPlayerViewBuilder implements Builder<StackPane> {
    private static final Image MUTED_ICON = ResourceHelper.loadImage("volume-x.png");
    private static final Image VOLUME_ICON = ResourceHelper.loadImage("volume-1.png");
    private static final Image PLAY_ICON = ResourceHelper.loadImage("play.png");
    private static final Image STOP_ICON = ResourceHelper.loadImage("pause.png");

    private final AudioPlayerModel model;
    private final Runnable onTogglePlay;
    private final Runnable onMutePressed;
    private final Runnable onVolumeChanged;
    private final Consumer<Double> onSeek;
    private final ViewLocalization localization = new ViewLocalization("language.audio_player", LanguageController.getSelectedLocale());

    @Override
    public StackPane build() {
        StackPane root = new StackPane();
        root.getStyleClass().add(Styles.BG_DEFAULT);

        BorderPane sideControls = new BorderPane();
        HBox infoHBox = createInfoHBox();
        HBox endHBox = createEndHBox();
        sideControls.setLeft(infoHBox);
        sideControls.setRight(endHBox);
        sideControls.setPickOnBounds(false);

        VBox centerControls = createPlayerControlsVBox();
        centerControls.setMaxWidth(Region.USE_PREF_SIZE);
        centerControls.setPickOnBounds(false);

        root.widthProperty().addListener((observable, oldValue, newValue) -> {
            double totalWidth = newValue.doubleValue();
            double available = (totalWidth / 2) - (centerControls.getWidth() / 2);
            infoHBox.setMaxWidth(available);
            endHBox.setMaxWidth(available);
        });

        root.getChildren().addAll(sideControls, centerControls);
        return root;
    }

    private HBox createEndHBox() {
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER_RIGHT);
        root.setPadding(new Insets(10));
        root.setMaxWidth(Region.USE_PREF_SIZE);

        ThemedIconButton muteButton = new ThemedIconButton(model.isMuted() ? MUTED_ICON : VOLUME_ICON, 18);
        muteButton.setPrefSize(24, 24);
        muteButton.getStyleClass().addAll(Styles.FLAT, Styles.BUTTON_CIRCLE);
        muteButton.setOnAction(_ -> onMutePressed.run());

        model.mutedProperty().addListener((_, _, isMuted) ->
                muteButton.getIconLabel().getImageView().setImage(isMuted ? MUTED_ICON : VOLUME_ICON));

        Slider volumeSlider = createHoverSlider();
        volumeSlider.setPrefWidth(100);
        volumeSlider.setMin(0.0);
        volumeSlider.setMax(1.0);
        volumeSlider.valueProperty().bindBidirectional(model.volumeProperty());
        volumeSlider.valueProperty().addListener((_, _, _) -> onVolumeChanged.run());

        root.getChildren().addAll(muteButton, volumeSlider);

        return root;
    }

    private VBox createPlayerControlsVBox() {
        VBox root = new VBox();
        root.setSpacing(5);
        root.setAlignment(Pos.CENTER);

        HBox buttonHBox = new HBox();
        buttonHBox.setAlignment(Pos.CENTER);
        buttonHBox.setSpacing(10);

        Button previousButton = new ThemedIconButton("skip-back.png", 18);
        previousButton.getStyleClass().addAll(Styles.FLAT, Styles.BUTTON_CIRCLE);
        previousButton.setOnAction(_ -> {
            Runnable onPlayPrevious = model.getOnPlayPrevious();
            if (onPlayPrevious != null) {
                onPlayPrevious.run();
            }
        });

        ThemedIconButton playButton = new ThemedIconButton("play.png", 20);
        playButton.setPrefSize(36, 36);
        playButton.getIconLabel().setStyle("-fx-text-fill: -color-fg-emphasis");
        playButton.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.ACCENT);
        playButton.setOnAction(_ -> onTogglePlay.run());

        model.playingProperty().addListener((_, _, isPlaying) ->
                playButton.getIconLabel().getImageView().setImage(isPlaying ? STOP_ICON : PLAY_ICON));

        Button nextButton = new ThemedIconButton("skip-forward.png", 18);
        nextButton.getStyleClass().addAll(Styles.FLAT, Styles.BUTTON_CIRCLE);
        nextButton.setOnAction(_ -> {
            Runnable onPlayNext = model.getOnPlayNext();
            if (onPlayNext != null) {
                onPlayNext.run();
            }
        });

        buttonHBox.getChildren().addAll(previousButton, playButton, nextButton);

        HBox progressHBox = new HBox();
        progressHBox.setSpacing(10);
        progressHBox.setAlignment(Pos.CENTER);

        Label currentTimeLabel = createTimeLabel(model.currentTimeTextProperty());

        Slider playTrackSlider = createHoverSlider();
        model.currentTimeSecondsProperty().addListener((_, _, newValue) -> {
            if (!playTrackSlider.isValueChanging() && !playTrackSlider.isPressed()) {
                playTrackSlider.setValue(newValue.doubleValue());
            }
        });
        playTrackSlider.setOnMouseReleased(_ -> {
            if (model.getCurrentTimeSeconds() != playTrackSlider.getValue()) onSeek.accept(playTrackSlider.getValue());
        });
        playTrackSlider.valueChangingProperty().addListener((_, oldValue, newValue) -> {
            if (oldValue && !newValue) onSeek.accept(playTrackSlider.getValue());
        });
        playTrackSlider.setMin(0.0);
        playTrackSlider.maxProperty().bind(model.totalTimeSecondsProperty());

        Label totalTimeLabel = createTimeLabel(model.totalTimeTextProperty());

        progressHBox.getChildren().addAll(currentTimeLabel, playTrackSlider, totalTimeLabel);

        root.getChildren().addAll(buttonHBox, progressHBox);
        return root;
    }

    private Label createTimeLabel(StringProperty textProperty) {
        Label timeLabel = new Label("00:00");
        textProperty.addListener((_, _, newText) -> {
            if (newText == null) newText = "00:00";
            timeLabel.setText(newText);
        });
        return timeLabel;
    }

    private Slider createHoverSlider() {
        Slider slider = new Slider();
        slider.getStyleClass().addAll(Styles.SMALL, "hover-slider");
        slider.setSkin(new ProgressSliderSkin(slider));

        return slider;
    }

    private HBox createInfoHBox() {
        HBox root = new HBox();
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(16));

        RoundedImageView imageView = new RoundedImageView();
        imageView.setFitWidth(50);
        model.coverImageBytesProperty().addListener((_, _, newValue) ->
                imageView.setImageBytes(newValue));

        VBox textVBox = new VBox();
        textVBox.setAlignment(Pos.CENTER_LEFT);

        Label songNameLabel = new Label();
        songNameLabel.setOnMouseEntered(_ -> songNameLabel.setUnderline(true));
        songNameLabel.setOnMouseExited(_ -> songNameLabel.setUnderline(false));
        songNameLabel.setOnMouseClicked(_ -> {
            Runnable onScrollToSong = model.getOnTitleClicked();
            if (onScrollToSong != null) onScrollToSong.run();
        });
        songNameLabel.setCursor(Cursor.HAND);
        songNameLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            String songName = model.getSongName();
            return songName == null ? localization.getForKey("no_song_selected") : songName;
        }, model.songNameProperty()));
        songNameLabel.getStyleClass().add(Styles.TEXT_BOLD);
        Tooltip songNameTooltip = new Tooltip();
        songNameTooltip.textProperty().bind(songNameLabel.textProperty());
        songNameLabel.setTooltip(songNameTooltip);

        Label artistLabel = new Label();
        artistLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            String artist = model.getArtist();
            return artist == null ? localization.getForKey("no_artist") : artist;
        }, model.artistProperty()));
        Tooltip artitsTooltip = new Tooltip();
        artitsTooltip.textProperty().bind(artistLabel.textProperty());
        artistLabel.setTooltip(artitsTooltip);

        textVBox.getChildren().addAll(songNameLabel, artistLabel);
        root.getChildren().addAll(imageView, textVBox);

        return root;
    }
}
