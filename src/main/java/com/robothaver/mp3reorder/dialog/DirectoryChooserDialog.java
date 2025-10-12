package com.robothaver.mp3reorder.dialog;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;

import java.io.File;

@RequiredArgsConstructor
public class DirectoryChooserDialog {
    private final Stage parentStage;
    private final String title;
    private final File initialDirectory;

    public File showDialog() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(initialDirectory);
        directoryChooser.setTitle(title);
        return directoryChooser.showDialog(parentStage);
    }
}
