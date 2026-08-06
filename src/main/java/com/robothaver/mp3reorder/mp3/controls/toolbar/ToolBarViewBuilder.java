package com.robothaver.mp3reorder.mp3.controls.toolbar;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Spacer;
import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.mp3.MP3Model;
import com.robothaver.mp3reorder.mp3.controls.NumberOfSongsViewBuilder;
import com.robothaver.mp3reorder.mp3.controls.ThemedIconButton;
import com.robothaver.mp3reorder.mp3.controls.search.SearchTextFieldController;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ToolBarViewBuilder implements Builder<ToolBar> {
    private final MP3Model model;
    private final Runnable onMoveSongToTop;
    private final Runnable onMoveSongUp;
    private final Runnable onMoveSongDown;
    private final Runnable onMoveSongToBottom;
    private final ViewLocalization localization = new ViewLocalization("language.toolbar", LanguageController.getSelectedLocale());

    @Override
    public ToolBar build() {
        NumberOfSongsViewBuilder songsViewBuilder = new NumberOfSongsViewBuilder(model.getSongs());
        HBox numberOfSongs = songsViewBuilder.build();

        CustomTextField searchTextField = createSearchTextField();
        searchTextField.promptTextProperty().bind(localization.bindString("search"));
        searchTextField.setPrefWidth(300);

        ToolBar toolBar = new ToolBar();
        toolBar.getItems().addAll(
                createButton("first_icon.png", localization.bindString("top"), onMoveSongToTop),
                createButton("up_icon.png", localization.bindString("up"), onMoveSongUp),
                createButton("down_icon.png", localization.bindString("down"), onMoveSongDown),
                createButton("last_icon.png", localization.bindString("bottom"), onMoveSongToBottom),
                new Separator(Orientation.VERTICAL),
                new Spacer(Orientation.HORIZONTAL),
                numberOfSongs,
                new Spacer(10),
                searchTextField
        );
        return toolBar;
    }

    private Button createButton(String icon, StringBinding localization, Runnable onAction) {
        ThemedIconButton button = new ThemedIconButton(icon, 20);
        button.getIconLabel().setStyle("-fx-text-fill: -color-fg-default");
        button.textProperty().bind(localization);
        button.setOnAction(_ -> onAction.run());

        return button;
    }

    private CustomTextField createSearchTextField() {
        return new SearchTextFieldController(model).getView();
    }
}
