package com.robothaver.mp3reorder.mp3_viewer.controls.detailes.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import lombok.Getter;


public class SongTextDataWidget implements Builder<Region> {
    @Getter
    private final StringProperty titleProperty = new SimpleStringProperty();
    private Property<String> activeBinding;
    @Getter
    private TextField textField;

    public SongTextDataWidget(String titleString) {
        titleProperty.set(titleString);
    }

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
        textField.promptTextProperty().bind(titleProperty);
        textField.setMaxWidth(Double.MAX_VALUE);

        Label titleLabel = new Label();
        titleLabel.textProperty().bind(titleProperty);
        vBox.getChildren().addAll(titleLabel, textField);

        return vBox;
    }

    public void setEditable(boolean editable) {
        if (textField != null) {
            textField.setEditable(editable);
        }
    }
}
