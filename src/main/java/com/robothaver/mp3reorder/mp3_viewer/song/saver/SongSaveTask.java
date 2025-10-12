package com.robothaver.mp3reorder.mp3_viewer.song.saver;

import com.mpatric.mp3agic.Mp3File;
import com.robothaver.mp3reorder.mp3_viewer.song.TagUtils;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import lombok.RequiredArgsConstructor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RequiredArgsConstructor
public class SongSaveTask implements Callable<SaveResult> {
    private final Song song;
    private final Path savePath;

    @Override
    public SaveResult call() {
        try {
            // Get what folder to save in
            String parentDir = getParentDir();

            // Write the data to the mp3 file's tag
            Mp3File mp3File = song.getMp3File();
            TagUtils.writeDataToTag(song);

            String newSongName = buildSongName();
            Path tempSavePath = Paths.get(parentDir, "EDITED_" + newSongName);
            Path newSavePath = Paths.get(parentDir, newSongName);

            // Save song under temporary name than rename it
            mp3File.save(tempSavePath.toString());
            Files.move(tempSavePath, newSavePath, REPLACE_EXISTING);

            // If the songs is saved to the existing location
            if (savePath == null) {
                Files.deleteIfExists(song.getPath());
            }

            return new SaveResult(null);
        } catch (Exception e) {
            e.printStackTrace();
            return new SaveResult(e);
        }
    }

    private String getParentDir() {
        if (savePath != null) {
            return savePath.toString();
        } else {
            return song.getPath().getParent().toString();
        }
    }

    private String buildSongName() {
        String newSongName = song.getFileName().trim();
        if (!newSongName.endsWith(".mp3")) {
            newSongName = newSongName + ".mp3";
        }
        return newSongName;
    }
}
