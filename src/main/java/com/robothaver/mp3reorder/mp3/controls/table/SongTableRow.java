package com.robothaver.mp3reorder.mp3.controls.table;

import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.mp3.domain.Song;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableRow;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Objects;

public class SongTableRow extends TableRow<Song> {
    private final MP3TableViewModel model;
    private final ViewLocalization localization = new ViewLocalization("language.table", LanguageController.getSelectedLocale());

    public SongTableRow(MP3TableViewModel model) {
        this.model = model;

        setOnMouseEntered(_ -> model.setHoveredIndex(getIndex()));
        setOnMouseExited(_ -> model.setHoveredIndex(-1));
        StringBinding styleBinding = Bindings.createStringBinding(() -> {
            if (Objects.equals(model.getSongInPlayer(), getItem())) {
                return "-fx-background-color: -color-accent-muted";
            }
            return "";
        }, model.songInPlayerProperty(), model.getSongs(), itemProperty());
        styleProperty().bind(styleBinding);
        setUserData(styleBinding);
        setContextMenu(createContextMenu());
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem revealInFolder = new MenuItem("Reveal in folder", new FontIcon(Feather.FOLDER));
        revealInFolder.setOnAction(_ -> tryExecuteRunnable(model.getOnRevealInFolder()));
        revealInFolder.textProperty().bind(localization.bindString("action.reveal_in_folder"));
        MenuItem openInDefaultPlayer = new MenuItem("Open in default player", new FontIcon(Feather.MUSIC));
        openInDefaultPlayer.setOnAction(_ -> tryExecuteRunnable(model.getOnOpenInDefaultPlayer()));
        openInDefaultPlayer.textProperty().bind(localization.bindString("action.open_in_default_player"));
        MenuItem play = new MenuItem("Play", new FontIcon(Feather.PLAY));
        play.textProperty().bind(localization.bindString("action.play"));
        play.setOnAction(_ -> {
            if (model.getOnTogglePlay() != null) model.getOnTogglePlay().accept(getIndex());
        });

        contextMenu.getItems().addAll(revealInFolder, openInDefaultPlayer, new SeparatorMenuItem(), play);

        return contextMenu;
    }

    private void tryExecuteRunnable(Runnable runnable) {
        if (runnable != null) runnable.run();
    }
}
