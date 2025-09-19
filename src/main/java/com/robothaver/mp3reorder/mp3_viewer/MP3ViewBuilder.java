package com.robothaver.mp3reorder.mp3_viewer;


import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import com.robothaver.mp3reorder.mp3_viewer.controls.SearchTextField;
import com.robothaver.mp3reorder.mp3_viewer.controls.table.MP3TableView;
import com.robothaver.mp3reorder.mp3_viewer.controls.detailes.SongDetailsSideBarViewBuilder;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.SongSearch;
import javafx.animation.PauseTransition;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Builder;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class MP3ViewBuilder implements Builder<Region> {
    private final MP3Model model;
    private final EventHandler<ActionEvent> onLoadSongs;
    private final Consumer<Integer> onMoveActiveSongUp;
    private final Consumer<Integer> onMoveActiveSongDown;
    private final BiConsumer<Integer, Integer> onTrackChanged;
    private final Runnable onSetTracksByFileName;
    private final Runnable onSearchQueryChanged;

    private TableView<Song> mp3FileTableView;


    @Override
    public Region build() {
        VBox controlsContainer = createBottomUI();
        ScrollPane sideContainer = (ScrollPane) new SongDetailsSideBarViewBuilder(model).build();

        SplitPane splitPane = new SplitPane(controlsContainer, sideContainer);
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.setDividerPositions(0.75);
        VBox.setVgrow(splitPane, Priority.ALWAYS);
        Button saveButton = new Button("Save", new FontIcon(Feather.SAVE));
        saveButton.setOnAction(event -> {
        });
        VBox mainContainer = new VBox();
        ToolBar toolBar = new ToolBar(
                getLoadButton(),
                saveButton,
                new Separator(Orientation.VERTICAL),
                new Button("Settings", new FontIcon(Feather.SETTINGS))
        );
        mainContainer.getChildren().addAll(toolBar, splitPane);

        return mainContainer;
    }

    private Button getLoadButton() {
        Button loadButton = new Button("Open", new FontIcon(Feather.FOLDER_PLUS));
        loadButton.setOnAction(event -> {
            Stage stage = (Stage) loadButton.getScene().getWindow();

            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File("."));
            directoryChooser.setTitle("Choose the Folder Containing Your Songs");
            File selectedDirectory = directoryChooser.showDialog(stage);

            if (selectedDirectory != null) {
                model.selectedPathProperty().setValue(selectedDirectory.getAbsolutePath());
                onLoadSongs.handle(event);
            }
        });
        return loadButton;
    }

    private VBox createBottomUI() {
        VBox vBox = new VBox();

        mp3FileTableView = new MP3TableView(model.getSongs(), this::changeTrack).build();

        mp3FileTableView.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldValue, newValue) -> {
            int index = (int) newValue;
            if (index != -1) {
                model.selectedSongIndexProperty().setValue(newValue);
                model.getSongs().get(index).init();
            }
        });

        HBox buttonContainer = new HBox();

        Button moveSongUpBtn = new Button("⬆️");
        moveSongUpBtn.setOnAction(e -> {
            int selectedIndex = mp3FileTableView.getSelectionModel().getSelectedIndex();
            onMoveActiveSongUp.accept(selectedIndex);
            selectIndex(selectedIndex - 1);
        });

        Button moveSongDownBtn = new Button("⬇️");
        moveSongDownBtn.setOnAction(e -> {
            int selectedIndex = mp3FileTableView.getSelectionModel().getSelectedIndex();
            onMoveActiveSongDown.accept(selectedIndex);
            selectIndex(selectedIndex + 1);
        });

        Button log = new Button("Log");
        log.setOnAction(e -> {
            Song selectedItem = mp3FileTableView.getSelectionModel().getSelectedItem();
            System.out.println(selectedItem);
        });

        Button setTracksButton = new Button("Set tracks by file name");
        setTracksButton.setOnAction(e -> {
            mp3FileTableView.getSortOrder().clear();
            onSetTracksByFileName.run();
        });

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

        buttonContainer.setSpacing(10);
        buttonContainer.getChildren().addAll(moveSongUpBtn, moveSongDownBtn, log, setTracksButton, searchTextField);

        Label numberOfSongsLabel = new Label("Songs: 0");

        model.getSongs().addListener((ListChangeListener<Song>) c ->
                numberOfSongsLabel.setText("Songs: " + model.getSongs().size())
        );

        vBox.getChildren().addAll(buttonContainer, numberOfSongsLabel, mp3FileTableView);
        return vBox;
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
