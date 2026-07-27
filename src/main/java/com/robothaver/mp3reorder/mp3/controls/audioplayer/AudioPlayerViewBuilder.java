package com.robothaver.mp3reorder.mp3.controls.audioplayer;

import atlantafx.base.controls.ProgressSliderSkin;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import com.robothaver.mp3reorder.core.utils.ResourceHelper;
import com.robothaver.mp3reorder.mp3.controls.RoundedImageView;
import com.robothaver.mp3reorder.mp3.controls.ThemedIconButton;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class AudioPlayerViewBuilder implements Builder<HBox> {
    private static final Image MUTED_ICON = ResourceHelper.loadImage("volume-x.png");
    private static final Image VOLUME_ICON = ResourceHelper.loadImage("volume-1.png");
    private static final Image PLAY_ICON = ResourceHelper.loadImage("play.png");
    private static final Image STOP_ICON = ResourceHelper.loadImage("pause.png");

    private final AudioPlayerModel model;
    private final Runnable onPlaySong;
    private final Runnable onMutePressed;
    private final Runnable onVolumeChanged;
    private final Consumer<Double> onSeek;

    @Override
    public HBox build() {
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        HBox.setHgrow(root, Priority.ALWAYS);
        root.getStyleClass().add(Styles.BG_DEFAULT);

        root.getChildren().addAll(createInfoHBox(), new Spacer(), createPlayerControlsVBox(), new Spacer(), createEndHBox());

        return root;
    }

    private HBox createEndHBox() {
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));

        ThemedIconButton muteButton = new ThemedIconButton(null, "volume-1.png", 18);
        muteButton.setPrefSize(24, 24);
        muteButton.getStyleClass().addAll(Styles.FLAT, Styles.BUTTON_CIRCLE);
        muteButton.setOnAction(_ -> onMutePressed.run());

        model.getIsMuted().addListener((_, _, isMuted) ->
                muteButton.getIconLabel().getImageView().setImage(isMuted ? MUTED_ICON : VOLUME_ICON));

        Slider volumeSlider = createHoverSlider();
        volumeSlider.setPrefWidth(100);
        volumeSlider.setMin(0.0);
        volumeSlider.setMax(1.0);
        volumeSlider.valueProperty().bindBidirectional(model.getVolumeProperty());
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

        Button previousButton = new ThemedIconButton(null, "skip-back.png", 18);
        previousButton.getStyleClass().addAll(Styles.FLAT, Styles.BUTTON_CIRCLE);
        ThemedIconButton playButton = new ThemedIconButton(null, "play.png", 20);
        playButton.setPrefSize(36, 36);
        playButton.getIconLabel().setStyle("-fx-text-fill: -color-fg-emphasis");
        playButton.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.ACCENT);
        playButton.setOnAction(_ -> onPlaySong.run());

        model.getIsPlaying().addListener((_, _, isPlaying) ->
                playButton.getIconLabel().getImageView().setImage(isPlaying ? STOP_ICON : PLAY_ICON));

        Button nextButton = new ThemedIconButton(null, "skip-forward.png", 18);
        nextButton.getStyleClass().addAll(Styles.FLAT, Styles.BUTTON_CIRCLE);

        buttonHBox.getChildren().addAll(previousButton, playButton, nextButton);

        HBox progressHBox = new HBox();
        progressHBox.setSpacing(10);
        progressHBox.setAlignment(Pos.CENTER);

        Label currentTimeLabel = new Label("0:00");
        currentTimeLabel.textProperty().bind(model.getCurrentTimeText());

        Slider playTrackSlider = createHoverSlider();
        model.getCurrentTime().addListener((_, _, newValue) -> {
            if (!playTrackSlider.isValueChanging() && !playTrackSlider.isPressed()) {
                playTrackSlider.setValue(newValue.doubleValue());
            }
        });
        playTrackSlider.setOnMouseReleased(_ -> {
            if (model.getCurrentTime().get() != playTrackSlider.getValue()) onSeek.accept(playTrackSlider.getValue());
        });
        playTrackSlider.valueChangingProperty().addListener((_, oldValue, newValue) -> {
            if (oldValue && !newValue) onSeek.accept(playTrackSlider.getValue());
        });
        playTrackSlider.setMin(0.0);
        playTrackSlider.maxProperty().bind(model.getTotalTime());

        Label totalTimeLabel = new Label("0:00");
        totalTimeLabel.textProperty().bind(model.getTotalTimeText());

        progressHBox.getChildren().addAll(currentTimeLabel, playTrackSlider, totalTimeLabel);

        root.getChildren().addAll(buttonHBox, progressHBox);
        return root;
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
        model.getCoverImageBytes().addListener((_, _, newValue) ->
                imageView.setImageBytes(newValue));

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
