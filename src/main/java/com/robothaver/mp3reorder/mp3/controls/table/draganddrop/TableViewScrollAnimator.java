package com.robothaver.mp3reorder.mp3.controls.table.draganddrop;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.skin.VirtualFlow;

public interface TableViewScrollAnimator {
    void setScrollDelta(double delta);

    void setRunning(boolean running);

    ObjectProperty<VirtualFlow<?>> virtualFlowProperty();
}
