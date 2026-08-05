package com.robothaver.mp3reorder.mp3;


import com.robothaver.mp3reorder.mp3.controls.StatusBar;
import com.robothaver.mp3reorder.mp3.controls.audioplayer.AudioPlayerController;
import com.robothaver.mp3reorder.mp3.controls.audioplayer.AudioPlayerModel;
import com.robothaver.mp3reorder.mp3.controls.details.SongDetailsSideMenuViewBuilder;
import com.robothaver.mp3reorder.mp3.controls.menubar.MenuBarController;
import com.robothaver.mp3reorder.mp3.controls.table.MP3TableViewController;
import com.robothaver.mp3reorder.mp3.controls.table.MP3TableViewModel;
import com.robothaver.mp3reorder.mp3.controls.toolbar.ToolBarController;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class MP3ViewBuilder implements Builder<Region> {
    private final MP3Model model;
    private final Runnable onLoadSongs;
    private final Runnable onCloseDetailsMenu;
    private final Runnable onPlayNext;
    private final Runnable onPlayPrevious;
    private final Runnable onScrollToPlaying;
    private final Consumer<Integer> onTogglePlay;

    @Override
    public Region build() {
        VBox baseContainer = new VBox();
        MenuBar menuBar = new MenuBarController(model, onLoadSongs).getView();

        VBox tableControls = createTableControls();
        VBox detailsSideMenu = new SongDetailsSideMenuViewBuilder(model, onCloseDetailsMenu).build();
        SplitPane splitPane = buildMainSplitPane(tableControls, detailsSideMenu);

        StatusBar statusBar = new StatusBar(model);
        BooleanProperty statusBarEnabled = model.getMenuBarModel().getStatusBarEnabled();
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
        ToolBar toolBar = new ToolBarController(model).getView();
        toolBar.setPrefHeight(50);

        MP3TableViewController tableViewController = new MP3TableViewController(model.getTrackEditor(), model.getSongs(), onTogglePlay);
        MP3TableViewModel tableViewModel = tableViewController.getModel();
        tableViewModel.selectedIndexProperty().bindBidirectional(model.selectedSongIndexProperty());
        tableViewModel.orderDescendingProperty().bindBidirectional(model.orderDescendingProperty());
        tableViewModel.songInPlayerProperty().bind(model.songInPlayerProperty());
        tableViewModel.songPlayingProperty().bind(model.songPlayingProperty());
        tableViewModel.scrollToSelectedProperty().bind(model.scrollToSelectedProperty());

        AudioPlayerController audioPlayerController = new AudioPlayerController();

        model.songInPlayerProperty().addListener((_, _, selectedSong) -> {
            if (selectedSong == null) return;
            audioPlayerController.playSong(selectedSong.getFileName(), selectedSong.getArtist(), selectedSong.getAlbumImage(), selectedSong.getPath().toString());
        });

        AudioPlayerModel audioPlayerModel = audioPlayerController.getModel();
        audioPlayerModel.playingProperty().bindBidirectional(model.songPlayingProperty());
        audioPlayerModel.setOnPlayNext(onPlayNext);
        audioPlayerModel.setOnPlayPrevious(onPlayPrevious);
        audioPlayerModel.setOnTitleClicked(onScrollToPlaying);

        tableContainer.getChildren().addAll(toolBar, tableViewController.getView(), audioPlayerController.getView());
        return tableContainer;
    }
}
