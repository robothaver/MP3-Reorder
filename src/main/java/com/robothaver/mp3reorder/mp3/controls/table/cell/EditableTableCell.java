package com.robothaver.mp3reorder.mp3.controls.table.cell;

import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

import java.util.Objects;
import java.util.function.BiConsumer;

public class EditableTableCell<S, T> extends TextFieldTableCell<S, T> {
    private final BiConsumer<T, T> onValueChanged;

    public EditableTableCell(BiConsumer<T, T> onValueChanged, StringConverter<T> stringConverter) {
        this.onValueChanged = onValueChanged;
        setConverter(stringConverter);
    }

    public static <S> EditableTableCell<S, String> forStringTableColumn(BiConsumer<String, String> onValueChanged) {
        return new EditableTableCell<>(onValueChanged, new DefaultStringConverter());
    }

    @Override
    public void commitEdit(T newValue) {
        T oldValue = getItem();

        super.commitEdit(newValue);

        if (!Objects.equals(oldValue, newValue) && onValueChanged != null) {
            onValueChanged.accept(oldValue, newValue);
        }
    }
}