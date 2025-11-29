package com.robothaver.mp3reorder.mp3_viewer.controls.detailes.controls;

import javafx.beans.property.Property;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@RequiredArgsConstructor
public class SongTextDataWidget implements Builder<Region> {
    private final String title;
    private Property<String> activeBinding;
    @Getter
    @Setter
    private boolean editable = true;
    @Getter
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
        textField.setEditable(editable);

        Label titleLabel = new Label(title);
        vBox.getChildren().addAll(titleLabel, textField);

        return vBox;
    }
}
