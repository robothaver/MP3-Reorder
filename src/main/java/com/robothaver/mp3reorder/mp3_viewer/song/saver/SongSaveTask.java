package com.robothaver.mp3reorder.mp3_viewer.song.saver;

import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.robothaver.mp3reorder.mp3_viewer.song.TagUtils;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RequiredArgsConstructor
public class SongSaveTask implements Callable<Void> {
    private final Song song;
    private final Path savePath;

    @Override
    public Void call() throws IOException, NotSupportedException {
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

        // TODO: If the path of the song gets edited than the mp3 file inside the song object
        //  will break as it will reference an old location.
        return null;
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
