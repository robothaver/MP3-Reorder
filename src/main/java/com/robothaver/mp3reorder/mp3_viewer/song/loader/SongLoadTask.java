package com.robothaver.mp3reorder.mp3_viewer.song.loader;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.robothaver.mp3reorder.mp3_viewer.domain.Song;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class SongLoadTask implements Callable<Song> {
    @Getter
    private final Path file;

    @Override
    public Song call() throws Exception {
        Mp3File mp3File = new Mp3File(file);
        int trackNumber = -1;
        String title = "";
        ID3v2 tag = mp3File.getId3v2Tag();
        if (mp3File.hasId3v2Tag()) {
            title = tag.getTitle();
            trackNumber = getTrackFromTag(mp3File);
        }
        return new Song(mp3File, file, trackNumber, title, tag);
    }

    private int getTrackFromTag(Mp3File file) {
        String track = file.getId3v2Tag().getTrack();
        if (track == null) {
            return -1;
        } else {
            try {
                return Integer.parseInt(track);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
    }
}
