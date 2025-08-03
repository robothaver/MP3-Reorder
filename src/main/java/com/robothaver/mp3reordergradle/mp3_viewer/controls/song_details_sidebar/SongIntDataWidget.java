package com.robothaver.mp3reordergradle.mp3_viewer.controls.song_details_sidebar;

import atlantafx.base.util.IntegerStringConverter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SongIntDataWidget implements Builder<Region> {
    private final String title;
    private final int min;
    private Property<Integer> activeBinding;
    private Spinner<Integer> spinner;


    public void bind(Property<Integer> newBinding) {
        if (spinner == null) return;

        ObjectProperty<Integer> objectProperty = spinner.getValueFactory().valueProperty();
        if (activeBinding != null) {
            objectProperty.unbindBidirectional(activeBinding);
        }

        objectProperty.bindBidirectional(newBinding);
        activeBinding = newBinding;
    }

    @Override
    public Region build() {
        HBox hBox = new HBox();
        hBox.setMaxWidth(Double.MAX_VALUE);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(title);

        spinner = new Spinner<>(min, Integer.MAX_VALUE, min);
        IntegerStringConverter.createFor(spinner);
        spinner.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_LEFT_VERTICAL);
        spinner.setPrefWidth(120);
        spinner.setEditable(true);

        hBox.getChildren().addAll(titleLabel, spinner);

        return hBox;
    }
}
