package com.robothaver.mp3reorder.mp3.song.load.task;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.robothaver.mp3reorder.mp3.domain.Song;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@Getter
@RequiredArgsConstructor
public class SongLoadTask implements Callable<Song> {
    private final Path file;

    @Override
    public Song call() throws Exception {
        Mp3File mp3File = new Mp3File(file, 65536, false);
        if (mp3File.hasId3v2Tag()) {
            ID3v2 tag = mp3File.getId3v2Tag();
            return new Song(file, getTrackFromTag(tag), tag.getTitle());
        } else {
            return new Song(file, -1, null);
        }
    }

    private int getTrackFromTag(ID3v2 tag) {
        String track = tag.getTrack();
        if (track == null) {
            return -1;
        } else {
            try {
                return Integer.parseInt(track);
            } catch (NumberFormatException _) {
                return -1;
            }
        }
    }
}
