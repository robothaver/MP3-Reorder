package com.robothaver.mp3reorder.mp3.controls.table.draganddrop;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TableRow;
import javafx.scene.control.skin.VirtualFlow;

public interface TableViewScrollAnimator<T> {
    void setScrollDelta(double delta);
    void setRunning(boolean running);
    ObjectProperty<VirtualFlow<TableRow<T>>> virtualFlowProperty();
}
