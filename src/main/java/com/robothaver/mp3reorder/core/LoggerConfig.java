package com.robothaver.mp3reorder.core;

public class LoggerConfig {

    private LoggerConfig() {
        /* This utility class should not be instantiated */
    }

    public static void setLoggerDirectory() {
        String os = System.getProperty("os.name").toLowerCase();
        String logDir;

        if (os.contains("mac")) {
            logDir = System.getProperty("user.home") + "/Library/Logs/MP3Reorder";
        } else if (os.contains("win")) {
            logDir = System.getenv("APPDATA") + "/MP3Reorder/logs";
        } else {
            logDir = System.getProperty("user.home") + "/.MP3Reorder/logs";
        }

        System.setProperty("appLogDir", logDir);
    }
}
