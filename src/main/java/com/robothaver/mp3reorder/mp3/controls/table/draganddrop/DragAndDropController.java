package com.robothaver.mp3reorder.mp3.controls.table.draganddrop;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TableRow;
import javafx.scene.control.skin.VirtualFlow;

public interface DragAndDropController<T> {
    void enableForTableView();

    void enableForTableRow(TableRow<T> tableRow);

    void setHandler(DragDroppedHandler handler);

    ObjectProperty<VirtualFlow<TableRow<T>>> virtualFlowProperty();

    @FunctionalInterface
    interface DragDroppedHandler {
        void handle(int originalIndex, int newIndex);
    }
}
