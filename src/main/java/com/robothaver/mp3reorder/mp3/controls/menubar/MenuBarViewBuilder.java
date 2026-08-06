package com.robothaver.mp3reorder.mp3.controls.menubar;

import com.robothaver.mp3reorder.core.ApplicationInfo;
import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Locale;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class MenuBarViewBuilder implements Builder<MenuBar> {
    private final MenuBarModel model;
    private final Consumer<Themes> onThemeChanged;
    private final Consumer<Locale> onLocaleChanged;
    private final Consumer<Integer> onSizeChanged;
    private final Runnable onOpenDirectory;
    private final Runnable onLaunchMaximizedChanged;
    private final Runnable onUseSystemMenuBar;
    private final Runnable onDetailsMenuStateChanged;
    private final Runnable onStatusBarStateChanged;
    private final Runnable onExit;
    private final Runnable onSetTracksByFileName;
    private final Runnable onRemoveIndexFromFileName;
    private final Runnable onSave;
    private final Runnable onSaveAs;

    private final MenuBar menuBar = new MenuBar();
    private int size = 1;


    private final ViewLocalization localization = new ViewLocalization("language.menubar", LanguageController.getSelectedLocale());

    @Override
    public MenuBar build() {
        menuBar.useSystemMenuBarProperty().bindBidirectional(model.getUseSystemMenuBar());
        menuBar.getMenus().addAll(
                createFileMenu(),
                createEditMenu(),
                createViewMenu()
        );
        return menuBar;
    }

    private Menu createFileMenu() {
        Menu fileMenu = new Menu();
        fileMenu.textProperty().bind(localization.bindString("file"));

        MenuItem openOption = createItem("Open", Feather.FOLDER, new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openOption.textProperty().bind(localization.bindString("file.open"));
        openOption.setOnAction(_ -> onOpenDirectory.run());
        MenuItem saveOption = createItem("Save", Feather.SAVE, new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveOption.textProperty().bind(localization.bindString("file.save"));
        saveOption.setOnAction(_ -> onSave.run());
        MenuItem saveAsOption = createItem("Save As", null, new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN));
        saveAsOption.textProperty().bind(localization.bindString("file.saveAs"));
        saveAsOption.setOnAction(_ -> onSaveAs.run());
        MenuItem exitOption = createItem("Exit", null, null);
        exitOption.textProperty().bind(localization.bindString("exit"));
        exitOption.setOnAction(_ -> onExit.run());
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
        editMenu.textProperty().bind(localization.bindString("edit"));
        MenuItem tracksByFilenameOption = createItem("Set tracks by filename", null, null);
        tracksByFilenameOption.textProperty().bind(localization.bindString("setTracksByFileName"));
        tracksByFilenameOption.setOnAction(_ -> onSetTracksByFileName.run());
        MenuItem removeIndexFromNameOption = createItem("Remove index from name", null, null);
        removeIndexFromNameOption.textProperty().bind(localization.bindString("removeIndexFromName"));
        removeIndexFromNameOption.setOnAction(_ -> onRemoveIndexFromFileName.run());
        editMenu.getItems().addAll(
                tracksByFilenameOption,
                removeIndexFromNameOption
        );
        return editMenu;
    }

    private Menu createViewMenu() {
        Menu viewMenu = new Menu("_View");
        viewMenu.textProperty().bind(localization.bindString("view"));

        CheckMenuItem launchMaximizedOption = new CheckMenuItem("Launch maximized", new FontIcon(Feather.MAXIMIZE));
        launchMaximizedOption.selectedProperty().bindBidirectional(model.getLaunchMaximized());
        launchMaximizedOption.onActionProperty().set(_ -> onLaunchMaximizedChanged.run());
        launchMaximizedOption.textProperty().bind(localization.bindString("launchMaximized"));

        CheckMenuItem useSystemMenuBarOption = new CheckMenuItem("Use system menu bar", new FontIcon(Feather.LAYOUT));
        useSystemMenuBarOption.selectedProperty().bindBidirectional(model.getUseSystemMenuBar());
        useSystemMenuBarOption.onActionProperty().set(_ -> onUseSystemMenuBar.run());
        useSystemMenuBarOption.textProperty().bind(localization.bindString("useSystemMenuBar"));

        CheckMenuItem detailsSideMenuOption = new CheckMenuItem("Details side menu", new FontIcon(Feather.SIDEBAR));
        detailsSideMenuOption.selectedProperty().bindBidirectional(model.getDetailsMenuEnabled());
        detailsSideMenuOption.onActionProperty().set(_ -> onDetailsMenuStateChanged.run());
        detailsSideMenuOption.textProperty().bind(localization.bindString("detailsSideMenu"));

        CheckMenuItem statusBarOption = new CheckMenuItem("Status bar", new FontIcon(Feather.INFO));
        statusBarOption.selectedProperty().bindBidirectional(model.getStatusBarEnabled());
        statusBarOption.onActionProperty().set(_ -> onStatusBarStateChanged.run());
        statusBarOption.textProperty().bind(localization.bindString("statusBar"));

        Menu themeMenu = new Menu("_Theme", new FontIcon(Feather.SUN));
        themeMenu.textProperty().bind(localization.bindString("theme"));
        for (Themes theme : Themes.values()) {
            CheckMenuItem themeMenuItem = new CheckMenuItem(theme.getDisplayName());
            themeMenuItem.setOnAction(_ -> onThemeChanged.accept(theme));
            themeMenu.getItems().add(themeMenuItem);
            if (model.getSelectedTheme().get().getTheme().equals(theme.getTheme())) {
                themeMenuItem.setSelected(true);
            }
        }

        model.getSelectedTheme().addListener((_, _, newValue) -> {
            for (MenuItem item : themeMenu.getItems()) {
                boolean selectedOption = item.getText().equals(newValue.getDisplayName());
                ((CheckMenuItem) item).setSelected(selectedOption);
            }
        });

        Menu languageOption = new Menu("Language", new FontIcon(Feather.GLOBE));
        languageOption.textProperty().bind(localization.bindString("language"));

        for (Locale supportedLocale : ApplicationInfo.SUPPORTED_LOCALES) {
            CheckMenuItem localeOption = new CheckMenuItem(supportedLocale.getDisplayName());
            localeOption.setSelected(model.getSelectedLocale().get().equals(supportedLocale));
            model.getSelectedLocale().addListener((_, _, newVal) ->
                    localeOption.setSelected(newVal.equals(supportedLocale))
            );

            localeOption.setOnAction(_ -> onLocaleChanged.accept(supportedLocale));
            languageOption.getItems().add(localeOption);
        }

        Menu sizeMenu = new Menu("_Size", new FontIcon(Feather.TYPE));
        sizeMenu.textProperty().bind(localization.bindString("size"));
        for (int i = 6; i < 26; i += 2) {
            sizeMenu.getItems().add(createSizeItem(i));
        }

        model.getSelectedSize().addListener((_, _, newValue) -> {
            for (MenuItem item : sizeMenu.getItems()) {
                boolean selectedOption = ((int) item.getUserData()) == newValue.intValue();
                ((CheckMenuItem) item).setSelected(selectedOption);
            }
        });

        viewMenu.getItems().addAll(
                detailsSideMenuOption,
                statusBarOption,
                new SeparatorMenuItem(),
                themeMenu,
                languageOption,
                sizeMenu,
                new SeparatorMenuItem(),
                launchMaximizedOption,
                useSystemMenuBarOption
        );
        return viewMenu;
    }

    private CheckMenuItem createSizeItem(int size) {
        CheckMenuItem sizeMenuItem = new CheckMenuItem(size + "px");
        sizeMenuItem.setOnAction(_ -> {
            onSizeChanged.accept(size);
            sizeMenuItem.setSelected((int) sizeMenuItem.getUserData() == model.getSelectedSize().get());
        });
        sizeMenuItem.setUserData(size);
        if (model.getSelectedSize().get() == size) {
            sizeMenuItem.setSelected(true);
        }
        return sizeMenuItem;
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
