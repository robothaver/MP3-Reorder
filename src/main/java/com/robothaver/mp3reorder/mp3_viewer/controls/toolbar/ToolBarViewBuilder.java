package com.robothaver.mp3reorder.mp3_viewer.controls.toolbar;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Spacer;
import com.robothaver.mp3reorder.LanguageController;
import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.mp3_viewer.ViewLocalization;
import com.robothaver.mp3reorder.mp3_viewer.controls.ImageButton;
import com.robothaver.mp3reorder.mp3_viewer.controls.NumberOfSongsViewBuilder;
import com.robothaver.mp3reorder.mp3_viewer.controls.search.SearchTextFieldController;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class ToolBarViewBuilder implements Builder<ToolBar> {
    private final MP3Model model;
    private final Runnable onMoveSongToTop;
    private final Runnable onMoveSongUp;
    private final Runnable onMoveSongDown;
    private final Runnable onMoveSongToBottom;
    private final Consumer<Integer> onSelectedIndexChanged;
    private final ViewLocalization localization = new ViewLocalization("language.toolbar", LanguageController.getSelectedLocale());

    @Override
    public ToolBar build() {
        Button moveSongToTop = new ImageButton("Top", "/icons/first_icon.png");
        moveSongToTop.textProperty().bind(localization.bindString("top"));
        moveSongToTop.setOnAction(e -> onMoveSongToTop.run());

        Button moveSongUpBtn = new ImageButton("Up", "/icons/up_icon.png");
        moveSongUpBtn.textProperty().bind(localization.bindString("up"));
        moveSongUpBtn.setOnAction(e -> onMoveSongUp.run());

        Button moveSongDownBtn = new ImageButton("Down", "/icons/down_icon.png");
        moveSongDownBtn.textProperty().bind(localization.bindString("down"));
        moveSongDownBtn.setOnAction(e -> onMoveSongDown.run());

        Button moveSongToBottom = new ImageButton("Bottom", "/icons/last_icon.png");
        moveSongToBottom.textProperty().bind(localization.bindString("bottom"));
        moveSongToBottom.setOnAction(e -> onMoveSongToBottom.run());

        NumberOfSongsViewBuilder songsViewBuilder = new NumberOfSongsViewBuilder(model.getSongs());
        HBox numberOfSongs = songsViewBuilder.build();

        CustomTextField searchTextField = createSearchTextField();
        searchTextField.promptTextProperty().bind(localization.bindString("search"));
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
