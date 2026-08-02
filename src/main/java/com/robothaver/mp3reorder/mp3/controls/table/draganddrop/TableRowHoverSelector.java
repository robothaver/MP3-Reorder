package com.robothaver.mp3reorder.mp3.controls.table.draganddrop;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TableRow;
import javafx.scene.control.skin.VirtualFlow;

public interface TableRowHoverSelector<T> {
    TableRow<T> getHoveredRow();
    void setRunning(boolean enabled);
    void setMousePosition(double x, double y);
    void setRowIndexToIgnore(int index);
    ObjectProperty<VirtualFlow<TableRow<T>>> virtualFlowProperty();
}
