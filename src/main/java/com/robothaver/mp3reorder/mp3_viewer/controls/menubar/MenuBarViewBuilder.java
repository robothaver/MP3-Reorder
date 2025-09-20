package com.robothaver.mp3reorder.mp3_viewer.controls.menubar;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class MenuBarViewBuilder implements Builder<MenuBar> {
    private final MenuBarModel model;
    private final Consumer<Themes> onThemeChanged;
    private CheckMenuItem selectedThemeOption;

    @Override
    public MenuBar build() {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(
                createFileMenu(),
                createEditMenu(),
                createViewMenu()
        );
        return menuBar;
    }

    private Menu createFileMenu() {
        Menu fileMenu = new Menu("_File");
        MenuItem openOption = createItem("Open", Feather.FOLDER, null);
        MenuItem saveOption = createItem("Save", Feather.SAVE, new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        MenuItem saveAsOption = createItem("Save As", null, new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN));
        saveAsOption.setOnAction(event -> {
            System.out.println("Save as option pressed!");
        });
        MenuItem exitOption = createItem("Exit", null, null);
        exitOption.setOnAction(event -> {
            System.exit(0);
        });
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
        MenuItem removeIndexFromNameOption = createItem("Remove index from name", null, null);
        editMenu.getItems().addAll(
                tracksByFilenameOption,
                removeIndexFromNameOption
        );
        return editMenu;
    }

    private Menu createViewMenu() {
        Menu viewMenu = new Menu("_View");

        CheckMenuItem detailsSideBar = new CheckMenuItem("Details side bar", new FontIcon(Feather.SIDEBAR));

        Menu themeMenu = new Menu("_Theme", new FontIcon(Feather.SUN));

        for (Themes theme : Themes.values()) {
            CheckMenuItem themeMenuItem = new CheckMenuItem(theme.getDisplayName());
            themeMenuItem.setOnAction(event -> onThemeChanged.accept(theme));
            themeMenu.getItems().add(themeMenuItem);
            if (model.getSelectedTheme().get().getTheme().equals(theme.getTheme())) {
                selectedThemeOption = themeMenuItem;
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

        MenuItem hungaryianOption = new CheckMenuItem("Magyar");
        MenuItem englishOption = new CheckMenuItem("English");

        languageOption.getItems().addAll(hungaryianOption, englishOption);

        viewMenu.getItems().addAll(
                detailsSideBar,
                new SeparatorMenuItem(),
                themeMenu,
                languageOption
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
