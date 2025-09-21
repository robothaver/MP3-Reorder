package com.robothaver.mp3reorder.mp3_viewer;


import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import com.robothaver.mp3reorder.mp3_viewer.controls.SearchTextField;
import com.robothaver.mp3reorder.mp3_viewer.controls.detailes.SongDetailsSideMenuViewBuilder;
import com.robothaver.mp3reorder.mp3_viewer.controls.menubar.MenuBarController;
import com.robothaver.mp3reorder.mp3_viewer.controls.table.MP3TableView;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.SongSearch;
import javafx.animation.PauseTransition;
import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class MP3ViewBuilder implements Builder<Region> {
    private final MP3Model model;
    private final Runnable onLoadSongs;
    private final Consumer<Integer> onMoveActiveSongUp;
    private final Consumer<Integer> onMoveActiveSongDown;
    private final BiConsumer<Integer, Integer> onTrackChanged;
    private final Runnable onSearchQueryChanged;

    private TableView<Song> mp3FileTableView;


    @Override
    public Region build() {
        VBox baseContainer = new VBox();
        MenuBar menuBar = new MenuBarController(model, onLoadSongs).getView();

        VBox tableControls = createTableControls();
        ScrollPane detailsSideMenu = (ScrollPane) new SongDetailsSideMenuViewBuilder(model).build();

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

        baseContainer.getChildren().addAll(menuBar, splitPane);
        return baseContainer;
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
        ToolBar toolBar = createToolBar();

        mp3FileTableView = new MP3TableView(model.getSongs(), this::changeTrack).build();
        mp3FileTableView.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldValue, newValue) -> {
            int index = (int) newValue;
            if (index != -1) {
                model.selectedSongIndexProperty().setValue(newValue);
                model.getSongs().get(index).init();
            }
        });

        tableContainer.getChildren().addAll(toolBar, mp3FileTableView);
        return tableContainer;
    }

    private CustomTextField createSearchTextField() {
        CustomTextField searchTextField = new SearchTextField().build();

        SongSearch songSearch = model.getSongSearch();
        searchTextField.textProperty().bindBidirectional(songSearch.getSearchQuery());

        PauseTransition pause = new PauseTransition(Duration.seconds(0.35));
        songSearch.getSearchQuery().addListener((observable, oldValue, newValue) -> {
            pause.setOnFinished(event -> {
                onSearchQueryChanged.run();
                selectIndex(model.getSelectedSongIndex());
            });
            pause.playFromStart();
        });
        songSearch.getFound().addListener((observable, oldValue, found) ->
                searchTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !found)
        );
        return searchTextField;
    }

    private ToolBar createToolBar() {
        Button moveSongUpBtn = new Button("Up", new FontIcon(Feather.ARROW_UP));
        moveSongUpBtn.setOnAction(e -> {
            int selectedIndex = mp3FileTableView.getSelectionModel().getSelectedIndex();
            onMoveActiveSongUp.accept(selectedIndex);
            selectIndex(selectedIndex - 1);
        });

        Button moveSongDownBtn = new Button("Down", new FontIcon(Feather.ARROW_DOWN));
        moveSongDownBtn.setOnAction(e -> {
            int selectedIndex = mp3FileTableView.getSelectionModel().getSelectedIndex();
            onMoveActiveSongDown.accept(selectedIndex);
            selectIndex(selectedIndex + 1);
        });

        Label numberOfSongs = new Label("Songs", new FontIcon(Feather.MUSIC));

        model.getSongs().addListener((ListChangeListener<Song>) c ->
                numberOfSongs.setText("Songs: " + model.getSongs().size())
        );

        CustomTextField searchTextField = createSearchTextField();
        searchTextField.setPrefWidth(300);

        ToolBar toolBar = new ToolBar();
        toolBar.setOrientation(Orientation.HORIZONTAL);
        toolBar.getItems().addAll(
                moveSongUpBtn,
                moveSongDownBtn,
                new Separator(Orientation.VERTICAL),
                new Spacer(Orientation.HORIZONTAL),
                numberOfSongs,
                new Spacer(10),
                searchTextField
        );
        return toolBar;
    }

    private void changeTrack(int track1, int track2) {
        onTrackChanged.accept(track1, track2);
        selectIndex(model.getSelectedSongIndex());
    }

    private void selectIndex(int index) {
        mp3FileTableView.getSelectionModel().select(index);
        mp3FileTableView.scrollTo(index - 5);
    }
}
