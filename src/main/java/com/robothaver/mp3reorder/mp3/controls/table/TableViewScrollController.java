package com.robothaver.mp3reorder.mp3.controls.table;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.skin.VirtualFlow;

public interface TableViewScrollController {
    void scrollToIndex(int index);

    ObjectProperty<VirtualFlow<?>> virtualFlowProperty();
}
