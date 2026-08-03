package com.robothaver.mp3reorder.mp3.controls.table;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.VirtualFlow;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TableViewScrollControllerImpl implements TableViewScrollController {
    private final ObjectProperty<VirtualFlow<?>> virtualFlowProperty = new SimpleObjectProperty<>();
    private final TableView<?> tableView;

    @Override
    public void scrollToIndex(int index) {
        Platform.runLater(() -> {
            VirtualFlow<?> flow = virtualFlowProperty.get();
            if (flow != null && flow.getFirstVisibleCell() != null && flow.getLastVisibleCell() != null) {
                int firstVisible = flow.getFirstVisibleCell().getIndex();
                int lastVisible = flow.getLastVisibleCell().getIndex();

                if (index <= firstVisible) {
                    flow.scrollToTop(index);
                } else if (index >= lastVisible) {
                    flow.scrollTo(index);
                }

            } else {
                tableView.scrollTo(index);
            }
        });
    }

    @Override
    public ObjectProperty<VirtualFlow<?>> virtualFlowProperty() {
        return virtualFlowProperty;
    }
}
