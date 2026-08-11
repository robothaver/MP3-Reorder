package com.robothaver.mp3reorder.mp3.song.save.task;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
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
    public Void call() throws IOException, NotSupportedException, InvalidDataException, UnsupportedTagException {
        Mp3File mp3File = new Mp3File(song.getPath());
        // Write the data to the mp3 file's tag
        TagUtils.writeDataToTag(song, mp3File);

        String newSongName = SongSaveUtils.createValidSongName(song);
        song.fileNameProperty().setValue(newSongName);

        Path newSavePath = Paths.get(savePath.toString(), newSongName);
        Files.deleteIfExists(newSavePath);
        mp3File.save(newSavePath.toString());

        return null;
    }
}
