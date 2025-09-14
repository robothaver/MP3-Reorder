package com.robothaver.mp3reordergradle.mp3_viewer.controls.detailes;

import javafx.beans.property.Property;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SongTextDataWidget implements Builder<Region> {
    private final String title;
    private Property<String> activeBinding;
    private TextField textField;

    public void bind(Property<String> newBinding) {
        if (textField == null) return;

        if (activeBinding != null) {
            textField.textProperty().unbindBidirectional(activeBinding);
        }

        textField.textProperty().bindBidirectional(newBinding);
        activeBinding = newBinding;
    }

    @Override
    public Region build() {
        VBox vBox = new VBox();
        vBox.setMaxWidth(Double.MAX_VALUE);
        vBox.setSpacing(3);

        textField = new TextField();
        textField.setPromptText(title);
        textField.setMaxWidth(Double.MAX_VALUE);

        Label titleLabel = new Label(title);
        vBox.getChildren().addAll(titleLabel, textField);

        return vBox;
    }
}
