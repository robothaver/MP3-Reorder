package com.robothaver.mp3reorder.mp3.controls;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.io.ByteArrayInputStream;
import java.util.Objects;

public class RoundedImageView extends ImageView {
    private static final Image DEFAULT_IMAGE = new Image(Objects.requireNonNull(RoundedImageView.class.getResourceAsStream("/images/no_image.png")));

    private final DoubleProperty borderRadius = new SimpleDoubleProperty(20);

    public RoundedImageView() {
        setPreserveRatio(true);
        setFitWidth(32);
        setFitHeight(Double.MAX_VALUE);
        setImage(DEFAULT_IMAGE);
        setClip(createRectangleClip());
    }

    public void setImageBytes(byte[] imageBytes) {
        Image image = imageBytes == null ? DEFAULT_IMAGE : new Image(new ByteArrayInputStream(imageBytes));
        Platform.runLater(() -> setImage(image));
    }

    private Rectangle createRectangleClip() {
        Rectangle clip = new Rectangle();

        clip.arcWidthProperty().bind(borderRadius);
        clip.arcHeightProperty().bind(borderRadius);
        clip.widthProperty().bind(Bindings.createDoubleBinding(() -> getLayoutBounds().getWidth(), layoutBoundsProperty()));
        clip.heightProperty().bind(Bindings.createDoubleBinding(() -> getLayoutBounds().getHeight(), layoutBoundsProperty()));
        return clip;
    }

    public double getBorderRadius() {
        return borderRadius.get();
    }

    public DoubleProperty borderRadiusProperty() {
        return borderRadius;
    }

    public void setBorderRadius(double borderRadius) {
        this.borderRadius.set(borderRadius);
    }
}
