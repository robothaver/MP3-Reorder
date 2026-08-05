package com.robothaver.mp3reorder.mp3.controls.table.cell;

import atlantafx.base.theme.Styles;
import com.robothaver.mp3reorder.core.utils.NodeUtils;
import com.robothaver.mp3reorder.core.utils.ResourceHelper;
import com.robothaver.mp3reorder.mp3.controls.ThemedIconButton;
import com.robothaver.mp3reorder.mp3.controls.table.MP3TableViewModel;
import com.robothaver.mp3reorder.mp3.domain.Song;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.image.Image;

import java.util.Objects;
import java.util.function.Consumer;

public class PlayTableCell extends TableCell<Song, Void> {
    private static final Image PLAY_ICON = ResourceHelper.loadImage("play.png");
    private static final Image PAUSE_ICON = ResourceHelper.loadImage("pause.png");

    private final MP3TableViewModel model;
    private final Consumer<Integer> onTogglePlay;

    private final ObservableValue<Song> cellSong;
    private final BooleanBinding isInPlayerBinding;
    private final ObjectBinding<Image> imageBinding;
    private final BooleanBinding visibilityBinding;

    public PlayTableCell(MP3TableViewModel model, Consumer<Integer> onTogglePlay) {
        this.model = model;
        this.onTogglePlay = onTogglePlay;
        this.cellSong = tableRowProperty().flatMap(TableRow::itemProperty);
        this.isInPlayerBinding = Bindings.createBooleanBinding(
                () -> Objects.equals(cellSong.getValue(), model.getSongInPlayer()),
                cellSong, model.songInPlayerProperty()
        );
        this.imageBinding = createImageBinding();
        this.visibilityBinding = createVisibilityBinding();

        setupCell();
        setGraphic(createIconButton());
    }

    private void setupCell() {
        NodeUtils.setNodeVisible(this, false);
        managedProperty().bind(visibilityBinding);
        visibleProperty().bind(visibilityBinding);
    }

    private ThemedIconButton createIconButton() {
        ThemedIconButton iconButton = new ThemedIconButton(PLAY_ICON, 16);
        iconButton.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_CIRCLE);
        iconButton.setOnAction(_ -> onTogglePlay.accept(getIndex()));
        iconButton.getIconLabel().getImageView().imageProperty().bind(imageBinding);

        isInPlayerBinding.addListener((_, _, isInPlayer) -> {
            if (isInPlayer) {
                iconButton.getStyleClass().add(Styles.FLAT);
                iconButton.getStyleClass().remove(Styles.ACCENT);
            } else {
                iconButton.getStyleClass().add(Styles.ACCENT);
                iconButton.getStyleClass().remove(Styles.FLAT);
            }
        });

        return iconButton;
    }

    private BooleanBinding createVisibilityBinding() {
        return Bindings.createBooleanBinding(
                () -> getIndex() == model.getHoveredIndex() || isInPlayerBinding.get(),
                model.hoveredIndexProperty(), indexProperty(), isInPlayerBinding
        );
    }

    private ObjectBinding<Image> createImageBinding() {
        return Bindings.createObjectBinding(
                () -> (isInPlayerBinding.get() && model.isSongPlaying()) ? PAUSE_ICON : PLAY_ICON,
                model.songPlayingProperty(), isInPlayerBinding
        );
    }
}