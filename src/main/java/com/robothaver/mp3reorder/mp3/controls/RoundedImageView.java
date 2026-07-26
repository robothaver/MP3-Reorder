package com.robothaver.mp3reorder.mp3.controls;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.io.ByteArrayInputStream;
import java.util.Objects;

public class RoundedImageView extends ImageView {
    private static final Image defaultImage = new Image(Objects.requireNonNull(RoundedImageView.class.getResourceAsStream("/images/no_image.png")));

    private final DoubleProperty imageFitWidthProperty = new SimpleDoubleProperty(32);
    private final DoubleProperty borderRadius = new SimpleDoubleProperty(20);

    public RoundedImageView() {
        setPreserveRatio(true);
        fitWidthProperty().bind(imageFitWidthProperty);
        setFitHeight(getImageFitWidth());
        setImage(defaultImage);
        setClip(createRectangleClip());
    }

    public void setImageBytes(byte[] imageBytes) {
        Image image = imageBytes == null ? defaultImage : new Image(new ByteArrayInputStream(imageBytes));
        setImage(image);
    }

    private Rectangle createRectangleClip() {
        Rectangle clip = new Rectangle();

        clip.arcWidthProperty().bind(borderRadius);
        clip.arcHeightProperty().bind(borderRadius);

        clip.widthProperty().bind(imageFitWidthProperty);
        clip.heightProperty().bind(Bindings.createDoubleBinding(this::calculateHeight, imageProperty(), imageFitWidthProperty));

        return clip;
    }

    private double calculateHeight() {
        Image img = getImage();
        if (img == null || img.getWidth() == 0 || img.getHeight() == 0) {
            return getImageFitWidth();
        }
        double aspectRatio = getImage().getWidth() / getImage().getHeight();
        return Math.min(getFitHeight(), imageFitWidthProperty.get() / aspectRatio);
    }

    public double getImageFitWidth() {
        return imageFitWidthProperty.get();
    }

    public DoubleProperty imageFitWidthProperty() {
        return imageFitWidthProperty;
    }

    public double getBorderRadius() {
        return borderRadius.get();
    }

    public DoubleProperty borderRadiusProperty() {
        return borderRadius;
    }

    public void setImageFitWidth(double imageFitWidthProperty) {
        this.imageFitWidthProperty.set(imageFitWidthProperty);
    }

    public void setBorderRadius(double borderRadius) {
        this.borderRadius.set(borderRadius);
    }
}
