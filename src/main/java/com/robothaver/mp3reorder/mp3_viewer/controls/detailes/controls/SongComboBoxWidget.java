package com.robothaver.mp3reorder.mp3_viewer.controls.detailes.controls;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import lombok.Getter;

public class SongComboBoxWidget<T> implements Builder<Region> {
    @Getter
    private final String title;
    @Getter
    private final ComboBox<T> comboBox;

    public SongComboBoxWidget(String title) {
        this.title = title;
        this.comboBox = new ComboBox<>();
        comboBox.setButtonCell(getButtonCell());
    }

    @Override
    public Region build() {
        VBox root = new VBox();
        root.setMaxWidth(Double.MAX_VALUE);
        root.setSpacing(3);
        comboBox.setMaxWidth(Double.MAX_VALUE);

        Label titleLabel = new Label(title);
        root.getChildren().addAll(titleLabel, comboBox);
        return root;
    }

    private ListCell<T> getButtonCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setText("Not set");
                } else {
                    setText(item.toString());
                }
            }
        };
    }
}
