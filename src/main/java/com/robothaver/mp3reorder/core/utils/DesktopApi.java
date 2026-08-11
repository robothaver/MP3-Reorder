package com.robothaver.mp3reorder.core.utils;

import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.dialog.DialogManagerImpl;
import javafx.scene.control.Alert;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;

@Log4j2
public class DesktopApi {
    private static final ViewLocalization localization = new ViewLocalization("language.table", LanguageController.getSelectedLocale());
    private static final Desktop desktop = getDesktop();

    private DesktopApi() {
        /* This utility class should not be instantiated */
    }

    public static void openInPlayer(Path path) {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.equals("linux")) {
                // desktop.isSupported returns true for OPEN, but it freezes the app
                runCommand("xdg-open", path.toAbsolutePath().toString());
            } else {
                if (desktop == null) {
                    showActionDesktopError(localization.getForKey("action.open_in_default_player"));
                } else if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(path.toFile());
                }
            }
        } catch (Exception e) {
            log.error("Failed to open song in default player", e);
            showActionError(localization.getForKey("action.open_in_default_player"), e);
        }
    }

    public static void revealInFolder(Path songPath) {
        try {
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE_FILE_DIR)) {
                desktop.browseFileDirectory(songPath.toFile());
                return;
            }

            String os = System.getProperty("os.name").toLowerCase();
            String path = songPath.toAbsolutePath().toString();

            if (os.contains("win")) {
                runCommand("cmd.exe", "/c", "explorer.exe /select,\"" + path + "\"");
            } else if (os.contains("mac")) {
                runCommand("open", "-R", path);
            } else {
                revealInFolderLinux(path, songPath);
            }
        } catch (Exception e) {
            log.error("Failed to reveal song in folder", e);
            showActionError(localization.getForKey("action.reveal_in_folder"), e);
        }
    }

    private static void revealInFolderLinux(String path, Path songPath) throws IOException, UnsupportedOperationException {
        boolean dbusSuccess = tryOpenWithDBus(songPath.toAbsolutePath().toUri());
        if (!dbusSuccess) {
            if (commandExists("dolphin")) {
                runCommand("dolphin", "--select", path);
            } else if (commandExists("nautilus")) {
                runCommand("nautilus", "--select", path);
            } else if (commandExists("xdg-open")) {
                runCommand("xdg-open", songPath.getParent().toString());
            }
        }
    }

    private static boolean tryOpenWithDBus(URI uri) {
        if (commandExists("dbus-send")) {
            try {
                runCommand(
                        "dbus-send",
                        "--session",
                        "--dest=org.freedesktop.FileManager1",
                        "--type=method_call",
                        "/org/freedesktop/FileManager1",
                        "org.freedesktop.FileManager1.ShowItems",
                        "array:string:" + uri,
                        "string:"
                );
                return true;
            } catch (Exception e) {
                log.warn("D-Bus FileManager1 call failed, falling back to direct commands", e);
            }
        }

        return false;
    }

    private static boolean commandExists(String command) {
        try {
            Process process = new ProcessBuilder("sh", "-c", "command -v " + command)
                    .redirectErrorStream(true)
                    .start();

            return process.waitFor() == 0;
        } catch (InterruptedException e) {
            log.warn("Interrupted!", e);
            Thread.currentThread().interrupt();
            return false;
        } catch (IOException _) {
            return false;
        }
    }

    private static void runCommand(String... command) throws IOException {
        new ProcessBuilder(command).start();
    }

    private static Desktop getDesktop() {
        try {
            return Desktop.getDesktop();
        } catch (Exception e) {
            log.error("Failed to get desktop", e);
        }
        return null;
    }

    private static void showActionDesktopError(String actionName) {
        showErrorDialog(localization.getForKey("action.unavailable"), localization.getForKey("desktop_error_message").formatted(actionName));
    }

    private static void showActionError(String actionName, Exception exception) {
        showErrorDialog(localization.getForKey("action.failed.title"), "%s %s".formatted(localization.getForKey("action.failed").formatted(actionName), exception));
    }

    private static void showErrorDialog(String title, String message) {
        DialogManagerImpl.getInstance().showAlert(Alert.AlertType.WARNING, title, message);
    }
}
