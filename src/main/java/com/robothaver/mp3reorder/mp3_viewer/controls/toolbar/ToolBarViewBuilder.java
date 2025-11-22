package com.robothaver.mp3reorder.mp3_viewer.controls.toolbar;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Spacer;
import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.mp3_viewer.controls.ImageButton;
import com.robothaver.mp3reorder.mp3_viewer.controls.search.SearchTextFieldController;
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
    private final Runnable onMoveSongToTop;
    private final Runnable onMoveSongUp;
    private final Runnable onMoveSongDown;
    private final Runnable onMoveSongToBottom;
    private final Consumer<Integer> onSelectedIndexChanged;

    @Override
    public ToolBar build() {
        Button moveSongToTop = new ImageButton("Top", "/icons/first_icon.png");
        moveSongToTop.setOnAction(e -> onMoveSongToTop.run());

        Button moveSongUpBtn = new ImageButton("Up", "/icons/up_icon.png");
        moveSongUpBtn.setOnAction(e -> onMoveSongUp.run());

        Button moveSongDownBtn = new ImageButton("Down", "/icons/down_icon.png");
        moveSongDownBtn.setOnAction(e -> onMoveSongDown.run());

        Button moveSongToBottom = new ImageButton("Bottom", "/icons/last_icon.png");
        moveSongToBottom.setOnAction(e -> onMoveSongToBottom.run());

        Label numberOfSongs = new Label("Songs", new FontIcon(Feather.MUSIC));

        model.getSongs().addListener((ListChangeListener<Song>) c ->
                numberOfSongs.setText("Songs: " + model.getSongs().size())
        );

        CustomTextField searchTextField = createSearchTextField();
        searchTextField.setPrefWidth(300);

        ToolBar toolBar = new ToolBar();
        toolBar.setOrientation(Orientation.HORIZONTAL);
        toolBar.getItems().addAll(
                moveSongToTop,
                moveSongUpBtn,
                moveSongDownBtn,
                moveSongToBottom,
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
