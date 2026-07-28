package com.robothaver.mp3reorder.mp3.controls.table;

import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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

        row.setOnDragEntered(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasContent(dataFormat) && !row.isEmpty()) {
                int draggedIndex = Integer.parseInt((String) db.getContent(dataFormat));
                int rowIndex = row.getIndex();

                if (rowIndex != draggedIndex) {
                    if (rowIndex > draggedIndex) {
                        row.setStyle("-fx-border-color: -color-accent-fg; -fx-border-width: 0 0 2 0;");
                    } else {
                        row.setStyle("-fx-border-color: -color-accent-fg; -fx-border-width: 2 0 0 0;");
                    }
                }

            }
        });

        row.setOnDragExited(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasContent(dataFormat)) row.setStyle("");
        });

        row.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasContent(dataFormat) && !row.isEmpty()) {
                int draggedIndex = Integer.parseInt((String) db.getContent(dataFormat));
                if (row.getIndex() != draggedIndex) {
                    event.acceptTransferModes(TransferMode.MOVE);
                    event.consume();
                }
            }
        });

        row.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasContent(dataFormat)) {
                event.setDropCompleted(true);
                event.consume();

                int originalIndex = Integer.parseInt((String) db.getContent(dataFormat));
                if (dragAndDropOverHandler != null) dragAndDropOverHandler.handle(originalIndex, row.getIndex());
            }
        });
    }

    @FunctionalInterface
    public interface DragAndDropOverHandler {
        void handle(int startIndex, int newIndex);
    }
}