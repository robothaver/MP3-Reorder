package com.robothaver.mp3reorder.mp3_viewer.controls.details.controls;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Builder;

import java.io.ByteArrayInputStream;
import java.util.Objects;

public class SongAlbumImageWidget implements Builder<VBox> {
    private final Image defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/no_image.png")));
    private ImageView imageView;
    private Rectangle clip;

    @Override
    public VBox build() {
        VBox imageContainer = new VBox();
        imageContainer.setMaxWidth(Double.MAX_VALUE);
        imageContainer.setAlignment(Pos.CENTER);

        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setImage(defaultImage);
        clip = new Rectangle(
                imageView.getFitWidth(), imageView.getFitHeight()
        );
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        imageView.setClip(clip);

        imageContainer.getChildren().add(imageView);

        return imageContainer;
    }

    public void setImage(byte[] imageBytes) {
        if (imageView == null) return;

        Image image;
        if (imageBytes == null) {
            image = defaultImage;
        } else {
            image = new Image(new ByteArrayInputStream(imageBytes));
        }
        imageView.setImage(image);
    }

    public void resizeImage(double width) {
        if (imageView == null || imageView.getImage() == null) return;
        double newWidth;
        if (width > 400) {
            newWidth = 370;
        } else {
            newWidth = width - 30;
        }
        imageView.setFitWidth(newWidth);
        imageView.setFitHeight(newWidth);
        clip.setWidth(newWidth);
        clip.setHeight(calculateHeight());
    }

    private double calculateHeight() {
        double aspectRatio = imageView.getImage().getWidth() / imageView.getImage().getHeight();
        return Math.min(imageView.getFitHeight(), imageView.getFitWidth() / aspectRatio);
    }
}
