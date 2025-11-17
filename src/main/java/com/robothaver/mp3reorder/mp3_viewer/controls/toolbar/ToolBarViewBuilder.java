package com.robothaver.mp3reorder.mp3_viewer.controls.toolbar;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Spacer;
import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.mp3_viewer.controls.serach.SearchTextFieldController;
import com.robothaver.mp3reorder.mp3_viewer.domain.Song;
import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class ToolBarViewBuilder implements Builder<ToolBar> {
    private final MP3Model model;
    private final Runnable onMoveSongUp;
    private final Runnable onMoveSongDown;
    private final Consumer<Integer> onSelectedIndexChanged;

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
        return new SearchTextFieldController(model, onSelectedIndexChanged).getView();
    }
}
