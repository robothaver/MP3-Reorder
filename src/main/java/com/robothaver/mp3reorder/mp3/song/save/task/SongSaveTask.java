package com.robothaver.mp3reorder.mp3.song.save.task;

import com.mpatric.mp3agic.*;
import com.robothaver.mp3reorder.mp3.song.TagUtils;
import com.robothaver.mp3reorder.mp3.domain.Song;
import com.robothaver.mp3reorder.mp3.song.save.SongSaveUtils;
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
        String newSongName = SongSaveUtils.createValidSongName(song);
        String parentDir = song.getPath().getParent().toString();
        Path newSavePath = Paths.get(parentDir, newSongName);

        if (song.getFileName().equals(newSongName)) {
            // The file has not been renamed
            saveWithExistingName(parentDir, newSongName, newSavePath);
        } else {
            // The file has a new name, have to remove the existing file
            saveWithNewName(newSavePath);
        }
        song.fileNameProperty().set(newSongName);

        // Have to re-read mp3 file to avoid byte change issues
        reloadMp3File(newSavePath);

        song.setFileChanged(false);
        return null;
    }

    private void saveWithExistingName(String parentDir, String newSongName, Path newSavePath) throws IOException, NotSupportedException {
        Path tempSavePath = Paths.get(parentDir, newSongName + ".tmp");
        mp3File.save(tempSavePath.toString());
        Files.move(tempSavePath, newSavePath, REPLACE_EXISTING);
    }

    private void saveWithNewName(Path newSavePath) throws IOException, NotSupportedException {
        mp3File.save(newSavePath.toString());
        if (!Files.isSameFile(song.getPath(), newSavePath)) Files.deleteIfExists(song.getPath());
        song.setPath(newSavePath);
    }

    private void reloadMp3File(Path savePath) throws InvalidDataException, UnsupportedTagException, IOException {
        Mp3File saveMp3File = new Mp3File(savePath);
        song.setTag(saveMp3File.getId3v2Tag());
        song.setMp3File(saveMp3File);
    }
}
