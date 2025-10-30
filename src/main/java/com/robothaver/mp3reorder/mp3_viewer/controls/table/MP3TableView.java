package com.robothaver.mp3reorder.mp3_viewer.controls.table;

import atlantafx.base.theme.Styles;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import com.robothaver.mp3reorder.mp3_viewer.utils.MP3FileUtils;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class MP3TableView implements Builder<TableView<Song>> {
    private final ObservableList<Song> songs;
    private final BiConsumer<Integer, Integer> onTrackChanged;
    private final BiConsumer<String, String> onFileRenamed;

    @Override
    public TableView<Song> build() {
        return createTable();
    }

    private TableView<Song> createTable() {
        TableView<Song> mp3FileTableView = new TableView<>();
        mp3FileTableView.setItems(songs);
        mp3FileTableView.setEditable(true);
        mp3FileTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);
        VBox.setVgrow(mp3FileTableView, javafx.scene.layout.Priority.ALWAYS);

        TableColumn<Song, Integer> trackColumn = getSongTrackTableColumn();

        TableColumn<Song, String> fileNameColumn = new TableColumn<>("File name");
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        fileNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fileNameColumn.setCellFactory(param ->
                EditableTableCell.forStringTableColumn(onFileRenamed)
        );
        fileNameColumn.setComparator(MP3FileUtils::compareFileNames);

        TableColumn<Song, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        mp3FileTableView.getColumns().add(trackColumn);
        mp3FileTableView.getColumns().add(fileNameColumn);
        mp3FileTableView.getColumns().add(titleColumn);

        Styles.toggleStyleClass(mp3FileTableView, Styles.BORDERED);

        return mp3FileTableView;
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
        return trackColumn;
    }
}
