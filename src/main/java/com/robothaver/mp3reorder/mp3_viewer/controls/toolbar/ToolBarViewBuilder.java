package com.robothaver.mp3reorder.mp3_viewer.controls.toolbar;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.mp3_viewer.controls.SearchTextField;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.SongSearch;
import javafx.animation.PauseTransition;
import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.util.Builder;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

@RequiredArgsConstructor
public class ToolBarViewBuilder implements Builder<ToolBar> {
    private final MP3Model model;
    private final Runnable onMoveSongUp;
    private final Runnable onMoveSongDown;
    private final Runnable onSearchQueryChanged;

    @Override
    public ToolBar build() {
        Button moveSongUpBtn = new Button("Up", new FontIcon(Feather.ARROW_UP));
        moveSongUpBtn.setOnAction(e -> onMoveSongUp.run());

        Button moveSongDownBtn = new Button("Down", new FontIcon(Feather.ARROW_DOWN));
        moveSongDownBtn.setOnAction(e -> onMoveSongDown.run());

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

    private CustomTextField createSearchTextField() {
        CustomTextField searchTextField = new SearchTextField().build();

        SongSearch songSearch = model.getSongSearch();
        searchTextField.textProperty().bindBidirectional(songSearch.getSearchQuery());

        PauseTransition pause = new PauseTransition(Duration.seconds(0.35));
        songSearch.getSearchQuery().addListener((observable, oldValue, newValue) -> {
            pause.setOnFinished(event -> onSearchQueryChanged.run());
            pause.playFromStart();
        });
        songSearch.getFound().addListener((observable, oldValue, found) ->
                searchTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !found)
        );
        return searchTextField;
    }
}
