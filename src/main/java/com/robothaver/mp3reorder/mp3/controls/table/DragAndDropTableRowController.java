package com.robothaver.mp3reorder.mp3.controls.table;

import javafx.scene.control.TableRow;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DragAndDropTableRowController<T> {
    private final DataFormat dataFormat;
    private DragAndDropOverHandler dragAndDropOverHandler;

    public DragAndDropTableRowController(TableRow<T> tableRow, DataFormat dataFormat) {
        this.dataFormat = dataFormat;
        enableDragAndDrop(tableRow);
    }

    private void enableDragAndDrop(TableRow<T> row) {
        row.setOnDragDetected(event -> {
            if (!row.isEmpty()) {
                Integer index = row.getIndex();
                Dragboard db = row.startDragAndDrop(TransferMode.MOVE);

                db.setDragView(row.snapshot(null, null));

                ClipboardContent cc = new ClipboardContent();
                cc.put(dataFormat, String.valueOf(index));
                db.setContent(cc);
                event.consume();
            }
        });

        row.setOnDragOver(event -> {
            if (true) return;
            Dragboard db = event.getDragboard();
            if (db.hasContent(dataFormat) && !row.isEmpty()) {
                int draggedIndex = Integer.parseInt((String) db.getContent(dataFormat));
                if (row.getIndex() != draggedIndex) {
                    event.acceptTransferModes(TransferMode.MOVE);
                    event.consume();
                }
            }
        });
    }

    @FunctionalInterface
    public interface DragAndDropOverHandler {
        void handle(int startIndex, int newIndex);
    }
}