package com.robothaver.mp3reorder.mp3.song.track.assigner;

import com.robothaver.mp3reorder.mp3.domain.Song;
import com.robothaver.mp3reorder.mp3.utils.MP3FileUtils;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class TrackAssignerImpl implements TrackAssigner {
    private final List<Song> songs;

    private final List<Song> songsWithoutTracks = new ArrayList<>();
    private final List<Song> songsWithTracks = new ArrayList<>();
    private final Set<Integer> tracks = new HashSet<>();
    private final Set<TrackIssue> issues = new HashSet<>();

    @Override
    public TrackAssignerResult assignTracks() {
        getSongsWithValidTracks();
        return new TrackAssignerResult(Arrays.asList(assignTracksWithExisting()), issues);
    }

    private void getSongsWithValidTracks() {
        songsWithTracks.clear();
        songsWithoutTracks.clear();
        tracks.clear();

        for (Song song : songs) {
            int track = song.getTrack();
            if (!isTrackValid(track)) {
                songsWithoutTracks.add(song);
            } else {
                songsWithTracks.add(song);
            }
        }
    }

    private boolean isTrackValid(int track) {
        if (track == -1) return false;

        if (track <= 0 || track > songs.size()) {
            issues.add(TrackIssue.TRACKS_IN_INVALID_RANGE);
            return false;
        }
        if (!tracks.add(track)) {
            issues.add(TrackIssue.DUPLICATE_TRACKS);
            return false;
        }
        return true;
    }

    private Song[] assignTracksWithExisting() {
        songsWithoutTracks.sort(this::compareFileNames);

        Song[] sortedSongArray = new Song[songs.size()];

        for (Song songsWithTrack : songsWithTracks) {
            sortedSongArray[songsWithTrack.getTrack() - 1] = songsWithTrack;
        }

        int songIndex = 0;
        for (int i = 0; i < sortedSongArray.length; i++) {
            if (sortedSongArray[i] == null) {
                Song withoutTracks = songsWithoutTracks.get(songIndex);
                withoutTracks.trackProperty().setValue(i + 1);
                sortedSongArray[i] = withoutTracks;
                songIndex++;
            }
        }

        return sortedSongArray;
    }

    private int compareFileNames(Song o1, Song o2) {
        return MP3FileUtils.compareFileNames(o2.getFileName(), o1.getFileName());
    }
}
