package com.robothaver.mp3reorder.mp3_viewer.controls.table;

import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

import java.util.function.BiConsumer;

public class EditableTableCell<S, T> extends TextFieldTableCell<S, T> {
    private final BiConsumer<T, T> onValueChanged;
    private T currentValue;
    private T newValue;

    public EditableTableCell(BiConsumer<T, T> onValueChanged, StringConverter<T> stringConverter) {
        this.onValueChanged = onValueChanged;
        setConverter(stringConverter);
    }

    public static <S> EditableTableCell<S, String> forStringTableColumn(BiConsumer<String, String> onValueChanged) {
        return new EditableTableCell<>(onValueChanged, new DefaultStringConverter());
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (currentValue == null) {
            currentValue = item;
        } else {
            currentValue = newValue;
        }
        newValue = item;
    }

    @Override
    public void commitEdit(T newValue) {
        super.commitEdit(newValue);
        if (!currentValue.equals(newValue)) {
            onValueChanged.accept(currentValue, newValue);
        }
    }
}
