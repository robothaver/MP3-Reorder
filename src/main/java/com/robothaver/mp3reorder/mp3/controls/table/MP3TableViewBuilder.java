package com.robothaver.mp3reorder.mp3.controls.table;

import atlantafx.base.theme.Styles;
import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.mp3.controls.table.cell.EditableTableCell;
import com.robothaver.mp3reorder.mp3.controls.table.cell.IntegerStringConverter;
import com.robothaver.mp3reorder.mp3.controls.table.cell.PlayTableCell;
import com.robothaver.mp3reorder.mp3.controls.table.draganddrop.DragAndDropController;
import com.robothaver.mp3reorder.mp3.controls.table.draganddrop.DragAndDropControllerImpl;
import com.robothaver.mp3reorder.mp3.controls.table.draganddrop.TableRowHoverSelectorImpl;
import com.robothaver.mp3reorder.mp3.controls.table.draganddrop.TableViewScrollAnimatorImpl;
import com.robothaver.mp3reorder.mp3.domain.Song;
import javafx.application.Platform;
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
    private static final DataFormat dataFormat = new DataFormat("mp3reorder-index");

    private final MP3TableViewModel model;
    private final BiConsumer<Integer, Integer> onTrackChanged;
    private final BiConsumer<String, String> onFileRenamed;
    private final BiConsumer<Integer, Integer> onMoveSong;
    private final ViewLocalization localization = new ViewLocalization("language.table", LanguageController.getSelectedLocale());

    private final ObjectProperty<VirtualFlow<TableRow<Song>>> virtualFlow = new SimpleObjectProperty<>(null);

    private TableView<Song> mp3TableView;
    private TableViewScrollController scrollController;
    private boolean changedByTable;

    @Override
    public TableView<Song> build() {
        return createTable();
    }

    private TableView<Song> createTable() {
        mp3TableView = new TableView<>();
        mp3TableView.setPlaceholder(createPlaceHolder());
        mp3TableView.setItems(model.getSongs());
        mp3TableView.setEditable(true);
        mp3TableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);
        VBox.setVgrow(mp3TableView, javafx.scene.layout.Priority.ALWAYS);
        Styles.toggleStyleClass(mp3TableView, Styles.BORDERED);

        getVirtualFlow();
        scrollController = new TableViewScrollControllerImpl(mp3TableView);
        scrollController.virtualFlowProperty().bind(virtualFlow);
        DragAndDropController<Song> dragAndDropController = createDragAndDropController();

        mp3TableView.setRowFactory(_ -> {
            SongTableRow row = new SongTableRow(model);
            dragAndDropController.enableForTableRow(row);
            return row;
        });
        setupSelectionListener();

        model.scrollToSelectedProperty().addListener((_, _, _) ->
                scrollController.scrollToIndex(model.getSelectedIndex()));

        TableColumn<Song, Integer> trackColumn = getSongTrackTableColumn();
        TableColumn<Song, String> fileNameColumn = createFileNameColumn();
        TableColumn<Song, String> titleColumn = createTitleColumn();

        TableColumn<Song, Void> playColumn = new TableColumn<>();
        playColumn.setMaxWidth(50);
        playColumn.setSortable(false);
        playColumn.setCellFactory(_ -> new PlayTableCell(model));

        ObservableList<TableColumn<Song, ?>> columns = mp3TableView.getColumns();
        columns.add(trackColumn);
        columns.add(fileNameColumn);
        columns.add(titleColumn);
        columns.add(playColumn);

        return mp3TableView;
    }

    private void setupSelectionListener() {
        mp3TableView.getSelectionModel().selectedIndexProperty().addListener((_, _, newValue) -> {
            changedByTable = true;
            model.setSelectedIndex(newValue.intValue());
        });
        model.selectedIndexProperty().addListener((_, _, newValue) -> {
            if (!changedByTable) selectAndScrollToIndex(newValue.intValue(), mp3TableView);
            changedByTable = false;
        });
    }

    private Label createPlaceHolder() {
        Label placeHolderLabel = new Label();
        placeHolderLabel.textProperty().bind(localization.bindString("placeholder"));
        return placeHolderLabel;
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
                model.setOrderDescending(newValue == TableColumn.SortType.DESCENDING));
        return trackColumn;
    }

    private DragAndDropController<Song> createDragAndDropController() {
        DragAndDropController<Song> dragAndDropController = new DragAndDropControllerImpl<>(mp3TableView, dataFormat, new TableViewScrollAnimatorImpl(), new TableRowHoverSelectorImpl<>());
        dragAndDropController.enableForTableView();
        dragAndDropController.setHandler(onMoveSong::accept);
        dragAndDropController.virtualFlowProperty().bind(virtualFlow);
        return dragAndDropController;
    }

    private void getVirtualFlow() {
        mp3TableView.skinProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                //noinspection unchecked
                Platform.runLater(() -> virtualFlow.set((VirtualFlow<TableRow<Song>>) mp3TableView.lookup(".virtual-flow")));
            }
        });
    }

    private void selectAndScrollToIndex(int index, TableView<?> tableView) {
        tableView.getSelectionModel().select(index);
        scrollController.scrollToIndex(index);
    }
}
