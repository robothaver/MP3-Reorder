package com.robothaver.mp3reorder.mp3_viewer.song.saver;

import com.mpatric.mp3agic.*;
import com.robothaver.mp3reorder.mp3_viewer.song.TagUtils;
import com.robothaver.mp3reorder.mp3_viewer.domain.Song;
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
    private Mp3File mp3File;

    @Override
    public Void call() throws IOException, NotSupportedException, InvalidDataException, UnsupportedTagException {
        mp3File = song.getMp3File();

        // Write the data to the mp3 file's tag
        TagUtils.writeDataToTag(song);

        // Get what folder to save in
        String parentDir = song.getPath().getParent().toString();
        String newSongName = SongSaveUtils.buildSongName(song);
        song.fileNameProperty().set(newSongName);

        Path newSavePath = Paths.get(parentDir, newSongName);
        if (song.getFileName().equals(song.getPath().getFileName().toString())) {
            // The file has not been renamed
            saveWithExistingName(parentDir, newSongName, newSavePath);
        } else {
            // The file has a new name, have to remove the existing file
            saveWithNewName(newSavePath);
        }
        // Have to re-read mp3 file to avoid size change issues
        reloadMp3File(newSavePath, mp3File.getId3v2Tag());

        song.getFileChanged().set(false);
        return null;
    }

    private void saveWithExistingName(String parentDir, String newSongName, Path newSavePath) throws IOException, NotSupportedException {
        Path tempSavePath = Paths.get(parentDir, "EDITED_" + newSongName);
        mp3File.save(tempSavePath.toString());
        Files.move(tempSavePath, newSavePath, REPLACE_EXISTING);
    }

    private void saveWithNewName(Path newSavePath) throws IOException, NotSupportedException {
        mp3File.save(newSavePath.toString());
        Files.deleteIfExists(song.getPath());
        song.setPath(newSavePath);
    }

    private void reloadMp3File(Path savePath, ID3v2 tag) throws InvalidDataException, UnsupportedTagException, IOException {
        Mp3File saveMp3File = new Mp3File(savePath);
        saveMp3File.setId3v2Tag(tag);
        song.setMp3File(saveMp3File);
    }
}
