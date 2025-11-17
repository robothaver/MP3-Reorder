package com.robothaver.mp3reorder.mp3_viewer.controls.serach;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import com.robothaver.mp3reorder.mp3_viewer.domain.Song;
import javafx.animation.PauseTransition;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.util.Builder;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

@RequiredArgsConstructor
public class SearchTextFieldViewBuilder implements Builder<CustomTextField> {
    private final SearchTextFieldModel searchState;
    private final Runnable onSelectPrevious;
    private final Runnable onSelectNext;
    private final Runnable onSearchQueryChanged;
    private final Runnable onClearSearch;

    @Override
    public CustomTextField build() {
        CustomTextField customTextField = new CustomTextField();
        customTextField.setMinWidth(500);
        customTextField.setPromptText("Search");
        customTextField.setLeft(new FontIcon(Feather.SEARCH));
        customTextField.setRight(createRightControls(customTextField.textProperty()));

        customTextField.textProperty().bindBidirectional(searchState.getSearchQuery());

        PauseTransition pause = new PauseTransition(Duration.seconds(0.35));
        searchState.getSearchQuery().addListener((observable, oldValue, newValue) -> {
            pause.setOnFinished(event -> onSearchQueryChanged.run());
            pause.playFromStart();
        });
        return customTextField;
    }

    private HBox createRightControls(StringProperty stringProperty) {
        HBox root = new HBox();
        root.setMinWidth(200);
        root.setSpacing(10);
        root.setVisible(false);
        root.setAlignment(Pos.CENTER);
        Label resultsLabel = new Label();
        resultsLabel.setMinWidth(50);
        resultsLabel.setAlignment(Pos.CENTER_RIGHT);
        searchState.getSelectedResultIndex().addListener((observable, oldValue, newValue) ->
                resultsLabel.setText((searchState.getSelectedResultIndex().get() + 1) + "/" + searchState.getResults().size())
        );

        Button previousBtn = new Button(null, new FontIcon(Feather.ARROW_UP));
        previousBtn.setDisable(true);
        previousBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.FLAT);
        previousBtn.setOnAction(event -> onSelectPrevious.run());
        Button nextBtn = new Button(null, new FontIcon(Feather.ARROW_DOWN));
        nextBtn.setDisable(true);
        nextBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.FLAT);
        nextBtn.setOnAction(event -> onSelectNext.run());
        Button clearButton = createClearButton();

        stringProperty.addListener((observable, oldValue, newValue) ->
                root.setVisible(!newValue.isBlank())
        );
        Separator separator = new Separator(Orientation.VERTICAL);
        separator.setPadding(new Insets(4, 0, 4, 0));

        searchState.getResults().addListener((ListChangeListener<? super Song>) c -> {
            boolean hasResults = searchState.getResults().isEmpty();
            previousBtn.setDisable(hasResults);
            nextBtn.setDisable(hasResults);
            resultsLabel.setText((searchState.getSelectedResultIndex().get() + 1) + "/" + searchState.getResults().size());
        });

        root.getChildren().addAll(
                resultsLabel,
                separator,
                previousBtn,
                nextBtn,
                clearButton
        );
        return root;
    }

    private Button createClearButton() {
        Button button = new Button(null, new FontIcon(Feather.X));
        button.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.FLAT);
        button.setOnAction(event -> onClearSearch.run());
        return button;
    }
}
