package com.robothaver.mp3reorder.mp3_viewer.controls;

import atlantafx.base.controls.Spacer;
import com.robothaver.mp3reorder.mp3_viewer.MP3Model;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import com.robothaver.mp3reorder.ApplicationInfo;
import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

public class StatusBar extends ToolBar {
    private final MP3Model model;

    public StatusBar(MP3Model model) {
        this.model = model;
        createUI();
    }

    private void createUI() {
        Label currentDirLabel = new Label();
        currentDirLabel.textProperty().bind(model.selectedPathProperty());
        FontIcon folderIcon = new FontIcon(Feather.FOLDER);

        FontIcon songIcon = new FontIcon(Feather.MUSIC);
        Label songCountLabel = new Label();
        model.getSongs().addListener((ListChangeListener<Song>) c ->
                songCountLabel.setText("Songs: " + model.getSongs().size())
        );

        Label appVersionText = new Label("v" + ApplicationInfo.APPLICATION_VERSION);
        getItems().addAll(
                folderIcon,
                currentDirLabel,
                new Separator(Orientation.VERTICAL),
                songIcon,
                songCountLabel,
                new Spacer(Orientation.HORIZONTAL),
                appVersionText
        );
    }
}
