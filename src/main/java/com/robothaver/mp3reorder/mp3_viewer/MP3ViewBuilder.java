package com.robothaver.mp3reorder.mp3_viewer;


import com.robothaver.mp3reorder.mp3_viewer.controls.StatusBar;
import com.robothaver.mp3reorder.mp3_viewer.controls.detailes.SongDetailsSideMenuViewBuilder;
import com.robothaver.mp3reorder.mp3_viewer.controls.menubar.MenuBarController;
import com.robothaver.mp3reorder.mp3_viewer.controls.table.MP3TableViewController;
import com.robothaver.mp3reorder.mp3_viewer.controls.toolbar.ToolBarController;
import com.robothaver.mp3reorder.mp3_viewer.domain.Song;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MP3ViewBuilder implements Builder<Region> {
    private final MP3Model model;
    private final Runnable onLoadSongs;

    private TableView<Song> mp3FileTableView;


    @Override
    public Region build() {
        VBox baseContainer = new VBox();
        MenuBar menuBar = new MenuBarController(model, onLoadSongs).getView();

        VBox tableControls = createTableControls();
        VBox detailsSideMenu = new SongDetailsSideMenuViewBuilder(model).build();
        SplitPane splitPane = buildMainSplitPane(tableControls, detailsSideMenu);

        StatusBar statusBar = new StatusBar(model);
        statusBar.visibleProperty().bind(model.getStatusBarEnabled());
        statusBar.managedProperty().bind(model.getStatusBarEnabled());

        baseContainer.getChildren().addAll(menuBar, splitPane, statusBar);
        return baseContainer;
    }

    private SplitPane buildMainSplitPane(VBox tableControls, VBox detailsSideMenu) {
        SplitPane splitPane = createSplitPane();
        splitPane.getItems().add(tableControls);

        if (model.getDetailsMenuEnabled().get()) {
            splitPane.getItems().add(detailsSideMenu);
        }

        model.getDetailsMenuEnabled().addListener((observable, oldValue, enabled) -> {
            if (enabled) {
                splitPane.getItems().add(detailsSideMenu);
            } else {
                splitPane.getItems().remove(detailsSideMenu);
            }
        });
        return splitPane;
    }

    private SplitPane createSplitPane() {
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.setDividerPositions(0.75);
        VBox.setVgrow(splitPane, Priority.ALWAYS);
        return splitPane;
    }

    private VBox createTableControls() {
        VBox tableContainer = new VBox();
        ToolBar toolBar = new ToolBarController(model, this::selectIndex).getView();
        toolBar.setPrefHeight(50);

        mp3FileTableView = new MP3TableViewController(model, this::selectIndex).getView();
        mp3FileTableView.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldValue, newValue) -> {
            int index = (int) newValue;
            if (index != -1) {
                model.selectedSongIndexProperty().setValue(newValue);
            }
        });

        tableContainer.getChildren().addAll(toolBar, mp3FileTableView);
        return tableContainer;
    }

    private void selectIndex(int index) {
        mp3FileTableView.getSelectionModel().select(index);
        mp3FileTableView.scrollTo(index - 5);
    }
}
