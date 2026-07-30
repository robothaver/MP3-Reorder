package com.robothaver.mp3reorder.mp3.controls.table;

import atlantafx.base.theme.Styles;
import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.mp3.domain.Song;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class MP3TableViewBuilder implements Builder<TableView<Song>> {
    private static final DataFormat dataFormat = new DataFormat("MP3Reorder/MP3TableView/Songs");
    private static final double NANOSECOND_TO_SECOND = 1_000_000_000.0;

    private final ObservableList<Song> songs;
    private final BooleanProperty orderDescending;
    private final IntegerProperty selectedIndex;
    private final BiConsumer<Integer, Integer> onTrackChanged;
    private final BiConsumer<String, String> onFileRenamed;
    private final BiConsumer<Integer, Integer> onMoveSong;
    private final ViewLocalization localization = new ViewLocalization("language.table", LanguageController.getSelectedLocale());

    private VirtualFlow<TableRow<Song>> virtualFlow;

    private double scrollModifier;
    private TableView<Song> mp3FileTableView;
    private boolean changedByTable;
    private double mouseX;
    private double mouseY;
    private TableRow<Song> selectedTableRow;
    private int draggedIndex;
    private Border oldBorder;
    private boolean isDragging;

    @Override
    public TableView<Song> build() {
        return createTable();
    }

    private TableView<Song> createTable() {
        mp3FileTableView = new TableView<>();
        Label placeHolderLabel = new Label();
        placeHolderLabel.textProperty().bind(localization.bindString("placeholder"));
        mp3FileTableView.setPlaceholder(placeHolderLabel);
        mp3FileTableView.setItems(songs);
        mp3FileTableView.setEditable(true);
        mp3FileTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);
        VBox.setVgrow(mp3FileTableView, javafx.scene.layout.Priority.ALWAYS);
        mp3FileTableView.setRowFactory(_ -> {
            TableRow<Song> row = new TableRow<>();
            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);

                    db.setDragView(row.snapshot(null, null));

                    isDragging = true;
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(dataFormat, String.valueOf(index));
                    db.setContent(cc);
                    event.consume();
                }
            });
            row.setOnDragDone(_ -> {
                isDragging = false;
            });
            //DragAndDropTableRowController<Song> dragAndDropController = new DragAndDropTableRowController<>(tableRow, dataFormat);
            //dragAndDropController.setDragAndDropOverHandler(onMoveSong::accept);
            return row;
        });
        mp3FileTableView.getSelectionModel().selectedIndexProperty().addListener((_, _, newValue) -> {
            changedByTable = true;
            selectedIndex.set(newValue.intValue());
        });
        selectedIndex.addListener((_, _, newValue) -> {
            if (!changedByTable) selectAndScrollToIndex(newValue.intValue(), mp3FileTableView);
            changedByTable = false;
        });

        TableColumn<Song, Integer> trackColumn = getSongTrackTableColumn();
        TableColumn<Song, String> fileNameColumn = createFileNameColumn();
        TableColumn<Song, String> titleColumn = createTitleColumn();

        ObservableList<TableColumn<Song, ?>> columns = mp3FileTableView.getColumns();
        columns.add(trackColumn);
        columns.add(fileNameColumn);
        columns.add(titleColumn);
        //mp3FileTableView.getColumns().add(playColumn);

        mp3FileTableView.addEventFilter(DragEvent.DRAG_EXITED, event -> {
            scrollModifier = 0;
        });
        mp3FileTableView.addEventFilter(DragEvent.DRAG_OVER, event -> {
            Dragboard db = event.getDragboard();
            if (!db.hasContent(dataFormat)) return;
            event.acceptTransferModes(TransferMode.MOVE);
            draggedIndex = Integer.parseInt((String) db.getContent(dataFormat));

            double height = mp3FileTableView.getLayoutBounds().getHeight();
            mouseY = event.getY();
            mouseX = event.getX();
            double minHeight = height * 0.9;
            if (mouseY >= minHeight) {
                scrollModifier = (mouseY - minHeight) / (height - minHeight);
            } else if (mouseY <= height * 0.3) {
                scrollModifier = -1 + (mouseY / (height * 0.3));
            } else {
                scrollModifier = 0.0;
            }
        });
        mp3FileTableView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasContent(dataFormat)) {
                event.setDropCompleted(true);
                event.consume();

                int originalIndex = Integer.parseInt((String) db.getContent(dataFormat));
                int newIndex = selectedTableRow.getItem().getTrack() - 1;
                onMoveSong.accept(originalIndex, newIndex);
            }
        });
        getAnimationTimer().start();

        Styles.toggleStyleClass(mp3FileTableView, Styles.BORDERED);

        return mp3FileTableView;
    }

    private TableColumn<Song, String> createTitleColumn() {
        TableColumn<Song, String> titleColumn = new TableColumn<>("Title");
        titleColumn.textProperty().bind(localization.bindString("title"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setSortable(false);
        return titleColumn;
    }

    private TableColumn<Song, String> createFileNameColumn() {
        TableColumn<Song, String> fileNameColumn = new TableColumn<>("File name");
        fileNameColumn.textProperty().bind(localization.bindString("file.name"));
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        fileNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fileNameColumn.setCellFactory(_ ->
                EditableTableCell.forStringTableColumn(onFileRenamed)
        );
        fileNameColumn.setSortable(false);
        return fileNameColumn;
    }

    private TableColumn<Song, Integer> getSongTrackTableColumn() {
        TableColumn<Song, Integer> trackColumn = new TableColumn<>("#");
        trackColumn.setCellValueFactory(new PropertyValueFactory<>("track"));
        trackColumn.setPrefWidth(10);
        trackColumn.setMinWidth(60);
        trackColumn.setMaxWidth(60);
        trackColumn.setCellFactory(songIntegerTableColumn ->
                new EditableTableCell<>(onTrackChanged, new IntegerStringConverter())
        );
        trackColumn.sortTypeProperty().addListener((_, _, newValue) ->
                orderDescending.set(newValue == TableColumn.SortType.DESCENDING));
        return trackColumn;
    }

    private void selectAndScrollToIndex(int index, TableView<?> tableView) {
        tableView.getSelectionModel().select(index);

        Platform.runLater(() -> {
            VirtualFlow<TableRow<Song>> flow = getFlow();
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

    private AnimationTimer getAnimationTimer() {
        return new AnimationTimer() {
            private final int basePixelsToMove = 200;
            private long lastUpdate = 0;

            @Override
            public void handle(long nowNs) {
                if (lastUpdate > 0) {
                    double deltaTime = (nowNs - lastUpdate) / NANOSECOND_TO_SECOND;

                    VirtualFlow<TableRow<Song>> flow = getFlow();
                    if (flow != null) {
                        flow.scrollPixels(basePixelsToMove * scrollModifier * deltaTime);

                        if (selectedTableRow != null) {
                            selectedTableRow.setBorder(oldBorder);
                        }
                        if (isDragging) {
                            TableRow<Song> row = getRowFromFlow(flow, mouseX, mouseY);

                            if (row != null) {
                                if (row.getIndex() != draggedIndex) {
                                    oldBorder = row.getBorder();
                                    BorderWidths widths = new BorderWidths(1, 0, 0, 0);
                                    BorderStroke stroke = new BorderStroke(
                                            Color.LIGHTSKYBLUE,
                                            BorderStrokeStyle.SOLID,
                                            CornerRadii.EMPTY,
                                            widths
                                    );
                                    row.setBorder(new Border(stroke));
                                }
                                selectedTableRow = row;
                            }
                        }
                    }
                }
                lastUpdate = nowNs;
            }
        };
    }

    public TableRow<Song> getRowFromFlow(VirtualFlow<TableRow<Song>> flow, double flowX, double flowY) {
        IndexedCell<?> first = flow.getFirstVisibleCell();
        IndexedCell<?> last = flow.getLastVisibleCell();
        if (first == null || last == null) return null;

        double headerHeight = mp3FileTableView.getLayoutBounds().getHeight() - flow.getHeight();
        Point2D scenePt = flow.localToScene(flowX, flowY - headerHeight);
        if (scenePt == null) return null;

        for (int i = first.getIndex(); i <= last.getIndex(); i++) {
            TableRow<Song> row = flow.getCell(i);
            Point2D localPt = row.sceneToLocal(scenePt);

            if (localPt != null && row.contains(localPt)) {
                return row;
            }
        }

        return null;
    }

    private VirtualFlow<TableRow<Song>> getFlow() {
        if (virtualFlow == null) {
            //noinspection unchecked
            Platform.runLater(() -> virtualFlow = (VirtualFlow<TableRow<Song>>) mp3FileTableView.lookup(".virtual-flow"));
        }

        return virtualFlow;
    }
}
