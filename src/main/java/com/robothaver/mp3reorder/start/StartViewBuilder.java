package com.robothaver.mp3reorder.start;

import atlantafx.base.theme.Styles;
import com.robothaver.mp3reorder.core.ApplicationInfo;
import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Objects;

@RequiredArgsConstructor
public class StartViewBuilder implements Builder<StackPane> {
    private static final Logger log = LogManager.getLogger(StartViewBuilder.class);
    private final Region mp3Region;
    private final StartModel model;
    private final Runnable onSelectFolder;
    private final ViewLocalization localization = new ViewLocalization("language.start", LanguageController.getSelectedLocale());

    @Override
    public StackPane build() {
        VBox startContent = createStartContent();
        startContent.visibleProperty().bind(model.getShowStart());
        StackPane stackPane = new StackPane(startContent);
        model.getShowStart().addListener((_, _, visible) -> {
            if (visible == null || visible) return;
            stackPane.getChildren().clear();
            stackPane.getChildren().add(mp3Region);
        });
        return stackPane;
    }

    private VBox createStartContent() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")));
        ImageView logo = new ImageView(image);
        logo.setFitWidth(200);
        logo.setFitHeight(200);
        Label greetLabel = new Label(localization.getForKey("greetLabel").formatted(ApplicationInfo.APPLICATION_NAME));
        greetLabel.getStyleClass().add(Styles.TITLE_2);
        Label descriptionLabel = new Label(localization.getForKey("descriptionLabel"));
        Button selectDirectory = new Button(localization.getForKey("selectDirectoryButton"), new FontIcon(Feather.FOLDER));
        selectDirectory.setOnAction(_ -> onSelectFolder.run());
        vBox.getChildren().addAll(logo, greetLabel, descriptionLabel, selectDirectory);
        return vBox;
    }
}
