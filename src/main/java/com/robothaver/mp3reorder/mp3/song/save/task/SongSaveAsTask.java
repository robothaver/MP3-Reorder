package com.robothaver.mp3reorder.mp3.song.save.task;

import com.mpatric.mp3agic.NotSupportedException;
import com.robothaver.mp3reorder.mp3.domain.Song;
import com.robothaver.mp3reorder.mp3.song.TagUtils;
import com.robothaver.mp3reorder.mp3.song.save.SongSaveUtils;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class SongSaveAsTask implements Callable<Void> {
    private final Song song;
    private final Path savePath;

    @Override
    public Void call() throws IOException, NotSupportedException {
        // Write the data to the mp3 file's tag
        TagUtils.writeDataToTag(song);

        String newSongName = SongSaveUtils.buildSongName(song);
        song.fileNameProperty().set(newSongName);

        Path newSavePath = Paths.get(savePath.toString(), newSongName);
        Files.deleteIfExists(newSavePath);
        song.getMp3File().save(newSavePath.toString());

        song.getFileChanged().set(false);
        return null;
    }
}
