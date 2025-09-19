package com.robothaver.mp3reorder.mp3_viewer.controls;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import javafx.scene.control.Button;
import javafx.util.Builder;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

public class SearchTextField implements Builder<CustomTextField> {
    @Override
    public CustomTextField build() {
        CustomTextField customTextField = new CustomTextField();
        customTextField.setPromptText("Search");
        customTextField.setLeft(new FontIcon(Feather.SEARCH));
        Button button = new Button(null, new FontIcon(Feather.X));
        button.setVisible(false);
        button.getStyleClass().addAll(
                Styles.BUTTON_CIRCLE, Styles.FLAT
        );
        button.setOnAction(event -> customTextField.textProperty().set(""));
        customTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            button.setVisible(!newValue.isBlank());
        });
        customTextField.setRight(button);
        return customTextField;
    }
}
