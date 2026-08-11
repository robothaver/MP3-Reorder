package com.robothaver.mp3reorder.mp3.controls.table.draganddrop;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.TableRow;
import javafx.scene.control.skin.VirtualFlow;

public interface TableRowHoverSelector<T> {
    TableRow<T> getHoveredRow();

    void setRunning(boolean enabled);

    void setMousePosition(Point2D mouseScenePosition);

    void setRowIndexToIgnore(int index);

    ObjectProperty<VirtualFlow<TableRow<T>>> virtualFlowProperty();
}
