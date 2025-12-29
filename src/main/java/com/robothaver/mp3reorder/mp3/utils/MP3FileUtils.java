package com.robothaver.mp3reorder.mp3.utils;

import com.robothaver.mp3reorder.core.language.LanguageController;
import com.robothaver.mp3reorder.core.language.ViewLocalization;
import com.robothaver.mp3reorder.dialog.DialogManagerImpl;

import java.io.File;
import java.text.Collator;
import java.util.Locale;

public class MP3FileUtils {
    private MP3FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final ViewLocalization localization = new ViewLocalization("language.dialog", LanguageController.getSelectedLocale());

    public static int compareFileNames(String source, String target) {
        int firstTrackNumber = getTrackNumberFromFileName(source);
        int secondTrackNumber = getTrackNumberFromFileName(target);
        // Neither name has a track
        if (firstTrackNumber == -1 && secondTrackNumber == -1) {
            Collator collator = Collator.getInstance(Locale.getDefault());
            return collator.compare(target, source);
        } else if (firstTrackNumber != -1 && secondTrackNumber != -1) {
            // Both names have a track
            return Integer.compare(secondTrackNumber, firstTrackNumber);
        } else if (firstTrackNumber != -1) {
            // The first name has a track the second one does not
            return 1;
        } else {
            return -1;
        }
    }

    public static int getTrackNumberFromFileName(String fileName) {
        char[] charArray = fileName.toCharArray();
        int lastNumIndex = 0;
        for (int i = 0; i < fileName.length(); i++) {
            if (!Character.isDigit(charArray[i])) {
                lastNumIndex = i;
                break;
            }
        }
        if (lastNumIndex != 0) {
            return Integer.parseInt(fileName.substring(0, lastNumIndex));
        } else {
            return -1;
        }
    }

    public static File chooseSongDirectory() {
        return DialogManagerImpl.getInstance()
                .showDirectoryChooserDialog(localization.getForKey("openSongDirDialogTitle"), new File("."));
    }
}
