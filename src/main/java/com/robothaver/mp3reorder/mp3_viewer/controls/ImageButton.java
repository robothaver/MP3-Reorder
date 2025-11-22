package com.robothaver.mp3reorder.mp3_viewer.controls;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.RequiredArgsConstructor;


import javafx.scene.control.Button;
import java.awt.*;
import java.util.Objects;

@RequiredArgsConstructor
public class ImageButton extends Button {
    public ImageButton(String label, String imagePath) throws HeadlessException {
        super(label);
        Image graphic = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        setGraphic(new ImageView(graphic));
    }
}
