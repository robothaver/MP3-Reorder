package com.robothaver.mp3reordergradle.mp3viewver.utils;

import java.text.Collator;
import java.util.Locale;

public class MP3FileUtils {
    public static int compareFileNames(String firstFile, String secondFile) {
        int firstTrackNumber = getTrackNumber(firstFile);
        int secondTrackNumber = getTrackNumber(secondFile);

        if (firstTrackNumber == -1 && secondTrackNumber != -1) {
            return 1;
        } else if (firstTrackNumber != -1 && secondTrackNumber == -1) {
            return -1;
        } else if (firstTrackNumber != -1) {
            return Integer.compare(firstTrackNumber, secondTrackNumber);
        } else {
            Collator collator = Collator.getInstance(Locale.getDefault());
            return collator.compare(firstFile, secondFile);
        }
    }

    public static int getTrackNumber(String fileName) {
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
}
