package com.robothaver.mp3reorder.mp3_viewer.song.saver;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
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
    public Void call() throws IOException, NotSupportedException, InvalidDataException, UnsupportedTagException {
        // Get what folder to save in
        String parentDir = getParentDir();

        // Write the data to the mp3 file's tag
        Mp3File mp3File = song.getMp3File();
        TagUtils.writeDataToTag(song);

        String newSongName = buildSongName();
        song.fileNameProperty().set(newSongName);

        Path newSavePath = Paths.get(parentDir, newSongName);
        if (savePath == null) {
            // The file is in the original directory
            if (song.getFileName().equals(song.getPath().getFileName().toString())) {
                // The file has not been renamed, just have to rename
                // the temp file (that will overwrite the existing song)
                Path tempSavePath = Paths.get(parentDir, "EDITED_" + newSongName);
                mp3File.save(tempSavePath.toString());
                Files.move(tempSavePath, newSavePath, REPLACE_EXISTING);
            } else {
                // The file has a new name, we have to remove the existing file
                mp3File.save(newSavePath.toString());
                Files.deleteIfExists(song.getPath());
                song.setPath(newSavePath);
            }
            // Have to re-read mp3 file to avoid size change issues
            Mp3File saveMp3File = new Mp3File(newSavePath);
            saveMp3File.setId3v2Tag(mp3File.getId3v2Tag());
            song.setMp3File(saveMp3File);
        } else {
            // The file has a new save path
            mp3File.save(newSavePath.toString());
        }
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
        if (!newSongName.toLowerCase().endsWith(".mp3")) {
            newSongName = newSongName + ".mp3";
        }
        return newSongName;
    }
}
