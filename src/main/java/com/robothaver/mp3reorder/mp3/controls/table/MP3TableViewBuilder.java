package com.robothaver.mp3reorder.mp3.controls.table;

import atlantafx.base.theme.Styles;
import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.mp3.controls.table.draganddrop.DragAndDropControllerImpl;
import com.robothaver.mp3reorder.mp3.controls.table.draganddrop.TableRowHoverSelectorImpl;
import com.robothaver.mp3reorder.mp3.controls.table.draganddrop.TableViewScrollAnimatorImpl;
import com.robothaver.mp3reorder.mp3.domain.Song;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class MP3TableViewBuilder implements Builder<TableView<Song>> {
    private static final DataFormat dataFormat = new DataFormat("MP3Reorder/MP3TableView/Songs");

    private final ObservableList<Song> songs;
    private final BooleanProperty orderDescending;
    private final IntegerProperty selectedIndex;
    private final BiConsumer<Integer, Integer> onTrackChanged;
    private final BiConsumer<String, String> onFileRenamed;
    private final BiConsumer<Integer, Integer> onMoveSong;
    private final ViewLocalization localization = new ViewLocalization("language.table", LanguageController.getSelectedLocale());

    private final ObjectProperty<VirtualFlow<TableRow<Song>>> virtualFlow = new SimpleObjectProperty<>(null);

    private TableView<Song> mp3TableView;
    private boolean changedByTable;

    @Override
    public TableView<Song> build() {
        return createTable();
    }

    private TableView<Song> createTable() {
        mp3TableView = new TableView<>();
        Label placeHolderLabel = new Label();
        placeHolderLabel.textProperty().bind(localization.bindString("placeholder"));
        mp3TableView.setPlaceholder(placeHolderLabel);
        mp3TableView.setItems(songs);
        mp3TableView.setEditable(true);
        mp3TableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);
        VBox.setVgrow(mp3TableView, javafx.scene.layout.Priority.ALWAYS);

        DragAndDropControllerImpl<Song> dragAndDropController = new DragAndDropControllerImpl<>(mp3TableView, dataFormat, new TableViewScrollAnimatorImpl<>(), new TableRowHoverSelectorImpl<>(mp3TableView));
        dragAndDropController.enableForTableView();
        dragAndDropController.setHandler(onMoveSong::accept);

        Platform.runLater(() -> virtualFlow.set((VirtualFlow<TableRow<Song>>) mp3TableView.lookup(".virtual-flow")));

        virtualFlow.bindBidirectional(dragAndDropController.virtualFlowProperty());

        mp3TableView.setRowFactory(_ -> {
            TableRow<Song> row = new TableRow<>();
            dragAndDropController.enableForTableRow(row);
            return row;
        });
        mp3TableView.getSelectionModel().selectedIndexProperty().addListener((_, _, newValue) -> {
            changedByTable = true;
            selectedIndex.set(newValue.intValue());
        });
        selectedIndex.addListener((_, _, newValue) -> {
            if (!changedByTable) selectAndScrollToIndex(newValue.intValue(), mp3TableView);
            changedByTable = false;
        });

        TableColumn<Song, Integer> trackColumn = getSongTrackTableColumn();
        TableColumn<Song, String> fileNameColumn = createFileNameColumn();
        TableColumn<Song, String> titleColumn = createTitleColumn();

        ObservableList<TableColumn<Song, ?>> columns = mp3TableView.getColumns();
        columns.add(trackColumn);
        columns.add(fileNameColumn);
        columns.add(titleColumn);
        //mp3FileTableView.getColumns().add(playColumn);
        Styles.toggleStyleClass(mp3TableView, Styles.BORDERED);

        return mp3TableView;
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
        trackColumn.setCellFactory(_ ->
                new EditableTableCell<>(onTrackChanged, new IntegerStringConverter())
        );
        trackColumn.sortTypeProperty().addListener((_, _, newValue) ->
                orderDescending.set(newValue == TableColumn.SortType.DESCENDING));
        return trackColumn;
    }

    private void selectAndScrollToIndex(int index, TableView<?> tableView) {
        tableView.getSelectionModel().select(index);

        Platform.runLater(() -> {
            VirtualFlow<TableRow<Song>> flow = virtualFlow.get();
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
}
