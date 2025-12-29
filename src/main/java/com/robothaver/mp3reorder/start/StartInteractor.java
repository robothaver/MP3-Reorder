package com.robothaver.mp3reorder.start;

import com.robothaver.mp3reorder.mp3.MP3Controller;
import com.robothaver.mp3reorder.mp3.utils.MP3FileUtils;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.nio.file.Paths;

@RequiredArgsConstructor
public class StartInteractor {
    private final StartModel model;
    private final MP3Controller mp3Controller;

    public void chooseSongLocation() {
        File file = MP3FileUtils.chooseSongDirectory();
        if (file != null) {
            model.getShowStart().set(false);
            mp3Controller.loadSongs(Paths.get(file.getAbsolutePath()));
        }
    }
}
