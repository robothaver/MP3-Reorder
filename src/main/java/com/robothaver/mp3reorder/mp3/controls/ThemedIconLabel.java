package com.robothaver.mp3reorder.mp3.controls;

import com.robothaver.mp3reorder.core.utils.ResourceHelper;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;


@Getter
public class ThemedIconLabel extends Label {
    private final ImageView imageView;
    private final DoubleProperty iconWidthProperty = new SimpleDoubleProperty(32);
    private final DoubleProperty iconHeightProperty = new SimpleDoubleProperty(32);

    public ThemedIconLabel(String text, String iconName) {
        this(text, ResourceHelper.loadImage(iconName));
    }

    public ThemedIconLabel(String text, Image icon) {
        this.imageView = new ImageView(icon);

        imageView.fitWidthProperty().bind(iconWidthProperty);
        imageView.fitHeightProperty().bind(iconHeightProperty);

        setGraphic(imageView);
        setText(text);
        createEffects();
    }

    public void setIconSize(double width, double height) {
        iconWidthProperty.set(width);
        iconHeightProperty.set(height);
    }

    private void createEffects() {
        ColorAdjust monochrome = new ColorAdjust();
        monochrome.setSaturation(-1.0);

        ColorInput colorInput = new ColorInput(0, 0, 32, 32, getTextFill());
        colorInput.heightProperty().bind(iconHeightProperty);
        colorInput.widthProperty().bind(iconWidthProperty);
        colorInput.paintProperty().bind(textFillProperty());

        Blend color = new Blend(
                BlendMode.SRC_ATOP,
                monochrome,
                colorInput
        );

        imageView.setEffect(color);
    }
}
