package com.robothaver.mp3reorder.mp3.controls.table.draganddrop;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class TableRowHoverSelectorImpl<T> implements TableRowHoverSelector<T> {
    private final ObjectProperty<VirtualFlow<TableRow<T>>> virtualFlowProperty = new SimpleObjectProperty<>();
    private final TableView<T> tableView;
    private final AnimationTimer animationTimer;

    private TableRow<T> lastHoveredRow;
    private Border oldBorder;
    private double mouseX;
    private double mouseY;
    private int rowIndexToIgnore = -1;

    public TableRowHoverSelectorImpl(TableView<T> tableView) {
        this.tableView = tableView;
        animationTimer = getAnimationTimer();
    }

    @Override
    public TableRow<T> getHoveredRow() {
        return lastHoveredRow;
    }

    @Override
    public void setRunning(boolean running) {
        if (running) animationTimer.start();
        else {
            animationTimer.stop();
            tryResetPreviousRowBorder();
        }
    }

    @Override
    public void setMousePosition(double x, double y) {
        mouseX = x;
        mouseY = y;
    }

    @Override
    public void setRowIndexToIgnore(int index) {
        rowIndexToIgnore = index;
    }

    @Override
    public ObjectProperty<VirtualFlow<TableRow<T>>> virtualFlowProperty() {
        return virtualFlowProperty;
    }

    private AnimationTimer getAnimationTimer() {
        return new AnimationTimer() {
            @Override
            public void handle(long nowNs) {
                if (virtualFlowProperty.get() != null) {
                    tryResetPreviousRowBorder();
                    TableRow<T> row = getRowFromFlow(virtualFlowProperty.get());

                    if (row != null && row.getIndex() != rowIndexToIgnore) {
                            oldBorder = row.getBorder();
                            BorderStroke stroke = getStroke(row);
                            row.setBorder(new Border(stroke));
                        }

                    lastHoveredRow = row;
                }

            }
        };
    }

    private void tryResetPreviousRowBorder() {
        if (lastHoveredRow != null) lastHoveredRow.setBorder(oldBorder);
    }

    private BorderStroke getStroke(TableRow<T> row) {
        BorderWidths widths;
        if (row.getIndex() > rowIndexToIgnore) {
            widths = new BorderWidths(0, 0, 1, 0);
        } else {
            widths = new BorderWidths(1, 0, 0, 0);
        }
        return new BorderStroke(
                Color.LIGHTSKYBLUE,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                widths
        );
    }

    public TableRow<T> getRowFromFlow(VirtualFlow<TableRow<T>> flow) {
        IndexedCell<?> first = flow.getFirstVisibleCell();
        IndexedCell<?> last = flow.getLastVisibleCell();
        if (first == null || last == null) return null;

        Point2D scenePt = tableView.localToScene(mouseX, mouseY);
        if (scenePt == null) return null;

        for (int i = first.getIndex(); i <= last.getIndex(); i++) {
            TableRow<T> row = flow.getCell(i);
            Point2D localPt = row.sceneToLocal(scenePt);

            if (localPt != null && row.contains(localPt)) {
                return row;
            }
        }

        return null;
    }
}
