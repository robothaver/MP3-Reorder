package com.robothaver.mp3reorder.mp3.controls.table.draganddrop;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import lombok.Getter;

public class DragAndDropControllerImpl<T> implements DragAndDropController<T> {
    private static final double SCROLL_UP_PERCENTAGE = 0.3;
    private static final double SCROLL_DOWN_PERCENTAGE = 0.9;

    private final TableView<T> tableView;
    private final DataFormat dataFormat;
    private final TableViewScrollAnimator scrollAnimator;
    private final TableRowHoverSelector<T> hoverSelector;
    private final ObjectProperty<VirtualFlow<TableRow<T>>> virtualFlowProperty = new SimpleObjectProperty<>();

    @Getter
    private DragDroppedHandler handler;

    public DragAndDropControllerImpl(TableView<T> tableView, DataFormat dataFormat, TableViewScrollAnimator scrollAnimator, TableRowHoverSelector<T> hoverSelector) {
        this.tableView = tableView;
        this.dataFormat = dataFormat;
        this.scrollAnimator = scrollAnimator;
        this.hoverSelector = hoverSelector;

        scrollAnimator.virtualFlowProperty().bind(virtualFlowProperty);
        hoverSelector.virtualFlowProperty().bind(virtualFlowProperty);
    }

    @Override
    public void enableForTableView() {
        tableView.setOnDragExited(_ -> {
            scrollAnimator.setScrollDelta(0);
            hoverSelector.setRunning(false);
        });
        tableView.setOnDragOver(event -> {
            if (!event.getDragboard().hasContent(dataFormat)) return;
            hoverSelector.setRunning(true);
            event.acceptTransferModes(TransferMode.MOVE);

            double mouseY = event.getY();
            double mouseX = event.getX();
            hoverSelector.setMousePosition(tableView.localToScene(mouseX, mouseY));
            scrollAnimator.setScrollDelta(getScrollDelta(mouseY));
        });
        tableView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasContent(dataFormat)) {
                event.setDropCompleted(true);
                event.consume();

                if (handler == null) return;

                int originalIndex = Integer.parseInt((String) db.getContent(dataFormat));
                TableRow<T> hoveredRow = hoverSelector.getHoveredRow();
                if (hoveredRow != null) {
                    int newIndex = hoveredRow.getIndex();
                    handler.handle(originalIndex, newIndex);
                }
            }
        });
    }

    @Override
    public void enableForTableRow(TableRow<T> tableRow) {
        tableRow.setOnDragDetected(event -> {
            if (!tableRow.isEmpty()) {
                Integer index = tableRow.getIndex();
                Dragboard db = tableRow.startDragAndDrop(TransferMode.MOVE);

                db.setDragView(tableRow.snapshot(null, null));

                ClipboardContent cc = new ClipboardContent();
                cc.put(dataFormat, String.valueOf(index));
                db.setContent(cc);
                event.consume();
                hoverSelector.setRowIndexToIgnore(tableRow.getIndex());
                hoverSelector.setRunning(true);
                scrollAnimator.setRunning(true);
            }
        });
        tableRow.setOnDragDone(_ -> {
            hoverSelector.setRunning(false);
            scrollAnimator.setRunning(false);
        });
    }

    @Override
    public void setHandler(DragDroppedHandler handler) {
        this.handler = handler;
    }

    @Override
    public ObjectProperty<VirtualFlow<TableRow<T>>> virtualFlowProperty() {
        return virtualFlowProperty;
    }

    private double getScrollDelta(double mouseY) {
        double height = tableView.getLayoutBounds().getHeight();
        double scrollDownHeight = height * SCROLL_DOWN_PERCENTAGE;
        double scrollUpHeight = height * SCROLL_UP_PERCENTAGE;

        double scrollDelta;
        if (mouseY >= scrollDownHeight) {
            scrollDelta = (mouseY - scrollDownHeight) / (height - scrollDownHeight);
        } else if (mouseY <= scrollUpHeight) {
            scrollDelta = -1 + (mouseY / scrollUpHeight);
        } else {
            scrollDelta = 0.0;
        }
        return scrollDelta;
    }
}
