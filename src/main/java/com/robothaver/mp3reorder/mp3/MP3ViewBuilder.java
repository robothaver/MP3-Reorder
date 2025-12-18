package com.robothaver.mp3reorder.mp3;


import com.robothaver.mp3reorder.mp3.controls.StatusBar;
import com.robothaver.mp3reorder.mp3.controls.details.SongDetailsSideMenuViewBuilder;
import com.robothaver.mp3reorder.mp3.controls.menubar.MenuBarController;
import com.robothaver.mp3reorder.mp3.controls.table.MP3TableViewController;
import com.robothaver.mp3reorder.mp3.controls.toolbar.ToolBarController;
import com.robothaver.mp3reorder.mp3.domain.Song;
import javafx.beans.property.BooleanProperty;
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
        BooleanProperty statusBarEnabled = model.getMenuBarModel().getStatusBarEnabled();
        statusBarEnabled.addListener((_, _, t1) -> System.out.println(t1));
        statusBar.visibleProperty().bind(statusBarEnabled);
        statusBar.managedProperty().bind(statusBarEnabled);

        baseContainer.getChildren().addAll(menuBar, splitPane, statusBar);
        return baseContainer;
    }

    private SplitPane buildMainSplitPane(VBox tableControls, VBox detailsSideMenu) {
        SplitPane splitPane = createSplitPane();
        splitPane.getItems().add(tableControls);
        BooleanProperty detailsMenuEnabled = model.getMenuBarModel().getDetailsMenuEnabled();
        if (detailsMenuEnabled.get()) {
            splitPane.getItems().add(detailsSideMenu);
        }

        detailsMenuEnabled.addListener((_, _, enabled) -> {
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
        mp3FileTableView.getSelectionModel().selectedIndexProperty().addListener((_, _, newValue) -> {
            int index = (int) newValue;
            if (index != -1) {
                model.selectedSongIndexProperty().setValue(newValue);
            }
        });

        tableContainer.getChildren().addAll(toolBar, mp3FileTableView);
        return tableContainer;
    }

    public void selectIndex(int index) {
        mp3FileTableView.getSelectionModel().select(index);
        mp3FileTableView.scrollTo(index - 5);
    }
}
