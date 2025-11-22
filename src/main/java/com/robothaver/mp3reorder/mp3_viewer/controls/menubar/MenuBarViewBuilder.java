package com.robothaver.mp3reorder.mp3_viewer.controls.menubar;

import javafx.beans.property.BooleanProperty;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class MenuBarViewBuilder implements Builder<MenuBar> {
    private final MenuBar menuBar = new MenuBar();

    private final MenuBarModel model;
    private final BooleanProperty detailsSideMenuEnabled;
    private final BooleanProperty statusBarEnabled;
    private final Consumer<Themes> onThemeChanged;
    private final BiConsumer<Parent, Size> onSizeChanged;
    private final Runnable onOpenDirectory;
    private final Runnable onExit;
    private final Runnable onSetTracksByFileName;
    private final Runnable onRemoveIndexFromFileName;
    private final Runnable onSave;

    @Override
    public MenuBar build() {
        menuBar.getMenus().addAll(
                createFileMenu(),
                createEditMenu(),
                createViewMenu()
        );
        return menuBar;
    }

    private Menu createFileMenu() {
        Menu fileMenu = new Menu("_File");
        MenuItem openOption = createItem("Open", Feather.FOLDER, new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openOption.setOnAction(event -> onOpenDirectory.run());
        MenuItem saveOption = createItem("Save", Feather.SAVE, new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveOption.setOnAction(event -> onSave.run());
        MenuItem saveAsOption = createItem("Save As", null, new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN));
        saveAsOption.setOnAction(event -> System.out.println("Save as option pressed!"));
        MenuItem exitOption = createItem("Exit", null, null);
        exitOption.setOnAction(event -> onExit.run());
        fileMenu.getItems().addAll(
                openOption,
                new SeparatorMenuItem(),
                saveOption,
                saveAsOption,
                new SeparatorMenuItem(),
                exitOption
        );
        return fileMenu;
    }

    private Menu createEditMenu() {
        Menu editMenu = new Menu("_Edit");

        MenuItem tracksByFilenameOption = createItem("Set tracks by filename", null, null);
        tracksByFilenameOption.setOnAction(event -> onSetTracksByFileName.run());
        MenuItem removeIndexFromNameOption = createItem("Remove index from name", null, null);
        removeIndexFromNameOption.setOnAction(event -> onRemoveIndexFromFileName.run());
        editMenu.getItems().addAll(
                tracksByFilenameOption,
                removeIndexFromNameOption
        );
        return editMenu;
    }

    private Menu createViewMenu() {
        Menu viewMenu = new Menu("_View");

        CheckMenuItem detailsSideMenuOption = new CheckMenuItem("Details side menu", new FontIcon(Feather.SIDEBAR));
        detailsSideMenuOption.selectedProperty().bindBidirectional(detailsSideMenuEnabled);

        CheckMenuItem statusBarOption = new CheckMenuItem("Status bar", new FontIcon(Feather.INFO));
        statusBarOption.selectedProperty().bindBidirectional(statusBarEnabled);

        Menu themeMenu = new Menu("_Theme", new FontIcon(Feather.SUN));

        for (Themes theme : Themes.values()) {
            CheckMenuItem themeMenuItem = new CheckMenuItem(theme.getDisplayName());
            themeMenuItem.setOnAction(event -> onThemeChanged.accept(theme));
            themeMenu.getItems().add(themeMenuItem);
            if (model.getSelectedTheme().get().getTheme().equals(theme.getTheme())) {
                themeMenuItem.setSelected(true);
            }
        }

        model.getSelectedTheme().addListener((observable, oldValue, newValue) -> {
            for (MenuItem item : themeMenu.getItems()) {
                boolean selectedOption = item.getText().equals(newValue.getDisplayName());
                ((CheckMenuItem) item).setSelected(selectedOption);
            }
        });

        Menu languageOption = new Menu("Language", new FontIcon(Feather.GLOBE));

        MenuItem hungarianOption = new CheckMenuItem("Magyar");
        MenuItem englishOption = new CheckMenuItem("English");

        languageOption.getItems().addAll(hungarianOption, englishOption);

        Menu sizeMenu = new Menu("_Size", new FontIcon(Feather.TYPE));
        for (Size size : Size.values()) {
            CheckMenuItem sizeMenuItem = new CheckMenuItem(size.getDisplayName());
            sizeMenuItem.setOnAction(event -> onSizeChanged.accept(menuBar.getScene().getRoot(), size));
            if (model.getSelectedSize().get().getFontSize() == size.getFontSize()) {
                sizeMenuItem.setSelected(true);
            }
            sizeMenu.getItems().add(sizeMenuItem);
        }

        model.getSelectedSize().addListener((observable, oldValue, newValue) -> {
            for (MenuItem item : sizeMenu.getItems()) {
                boolean selectedOption = item.getText().equals(newValue.getDisplayName());
                ((CheckMenuItem) item).setSelected(selectedOption);
            }
        });

        viewMenu.getItems().addAll(
                detailsSideMenuOption,
                statusBarOption,
                new SeparatorMenuItem(),
                themeMenu,
                languageOption,
                sizeMenu
        );
        return viewMenu;
    }

    private MenuItem createItem(String text, Ikon icon, KeyCombination accelerator) {
        MenuItem menuItem = new MenuItem(text);
        if (icon != null) {
            menuItem.setGraphic(new FontIcon(icon));
        }

        if (accelerator != null) {
            menuItem.setAccelerator(accelerator);
        }

        return menuItem;
    }
}
