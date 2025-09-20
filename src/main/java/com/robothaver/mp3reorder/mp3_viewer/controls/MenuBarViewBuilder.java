package com.robothaver.mp3reorder.mp3_viewer.controls;

import atlantafx.base.theme.*;
import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.util.Builder;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.HashMap;
import java.util.Map;

public class MenuBarViewBuilder implements Builder<MenuBar> {
    private Theme selectedTheme = new PrimerDark();
    private HashMap<Theme, CheckMenuItem> themes = new HashMap<>();

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

        Menu themeMenu = new Menu("_Theme");
        CheckMenuItem cupertinoDark = new CheckMenuItem("Cupertino dark");
        themes.put(new CupertinoDark(), cupertinoDark);
        CheckMenuItem cupertinoLight = new CheckMenuItem("Cupertino light");
        themes.put(new CupertinoLight(), cupertinoLight);
        CheckMenuItem nordDark = new CheckMenuItem("Nord dark");
        themes.put(new NordDark(), nordDark);
        CheckMenuItem nordLight = new CheckMenuItem("Nord light");
        themes.put(new NordLight(), nordLight);
        CheckMenuItem primerDark = new CheckMenuItem("Primer dark");
        themes.put(selectedTheme, primerDark);
        CheckMenuItem primerLight = new CheckMenuItem("Primer light");
        themes.put(new PrimerLight(), primerLight);
        CheckMenuItem dracula = new CheckMenuItem("Dracula");
        themes.put(new Dracula(), dracula);

        for (Map.Entry<Theme, CheckMenuItem> themeEntry : themes.entrySet()) {
            themeEntry.getValue().setOnAction(event -> setTheme(themeEntry.getKey()));
        }

        themeMenu.getItems().addAll(
                cupertinoDark,
                cupertinoLight,
                nordDark,
                nordLight,
                primerDark,
                primerLight,
                dracula
        );

        viewMenu.getItems().addAll(
                detailsSideBar,
                new SeparatorMenuItem(),
                themeMenu
        );
        return viewMenu;
    }

    private void setTheme(Theme theme) {
        themes.get(selectedTheme).setSelected(false);
        Application.setUserAgentStylesheet(theme.getUserAgentStylesheet());
        themes.get(theme).setSelected(true);
        selectedTheme = theme;
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
