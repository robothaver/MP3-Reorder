package com.robothaver.mp3reorder.mp3_viewer.controls.table;

import javafx.util.StringConverter;

public class IntegerStringConverter extends StringConverter<Integer> {
    private Integer currentValue;

    @Override
    public String toString(Integer integer) {
        currentValue = integer;
        return Integer.toString(integer);
    }

    @Override
    public Integer fromString(String s) {
        try {
            int parsed = Integer.parseInt(s);
            currentValue = parsed;
            return parsed;
        } catch (NumberFormatException e) {
            return currentValue;
        }
    }
}
