package com.robothaver.mp3reorder.mp3_viewer.song.track.assigner;

import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import com.robothaver.mp3reorder.mp3_viewer.utils.MP3FileUtils;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class TrackAssignerImpl implements TrackAssigner {
    private final List<Song> songsWithTracks = new ArrayList<>();
    private final List<Song> songsWithoutTracks = new ArrayList<>();
    private final List<Song> songs;

    @Override
    public TrackAssignerResult assignTracks() {
        separateSongsByTrack();
        TrackIssue trackIssue = detectTrackIssue();
        List<Song> processedSongs;
        if (trackIssue == TrackIssue.NONE) {
            processedSongs = assignTracksWithExisting(songs);
        } else {
            processedSongs = assignNewTracks(songs);
        }
        System.out.println("Used existing tracks: " + trackIssue);
        return new TrackAssignerResult(processedSongs, trackIssue);
    }

    private List<Song> assignNewTracks(List<Song> songs) {
        List<Song> sorted = songs.stream()
                .sorted(this::compareFileNames)
                .toList();
        for (int i = 0; i < sorted.size(); i++) {
            sorted.get(i).setTrack(i + 1);
        }
        return sorted;
    }

    private List<Song> assignTracksWithExisting(List<Song> songs) {
        List<Song> sortedSongs = new ArrayList<>();
        songsWithoutTracks.sort(this::compareFileNames);

        for (int i = 0; i < songs.size(); i++) {
            // If there is a song with a track that is equal to the current index
            Song songWithTrack = getSongWithTrack(songsWithTracks, i + 1);
            if (songWithTrack != null) {
                sortedSongs.add(songWithTrack);
            } else {
                // Else add the next song without track and update its track
                Song songWithoutTrack = songsWithoutTracks.getFirst();
                songWithoutTrack.setTrack(i + 1);
                sortedSongs.add(songWithoutTrack);
                songsWithoutTracks.removeFirst();
            }
        }

        return sortedSongs;
    }

    private TrackIssue detectTrackIssue() {
        if (!areTracksUnique()) {
            return TrackIssue.DUPLICATE_TRACKS;
        } else if (!areTracksInValidRange()) {
            return TrackIssue.TRACKS_IN_INVALID_RANGE;
        } else {
            return TrackIssue.NONE;
        }
    }

    private boolean areTracksInValidRange() {
        for (Song song : songsWithTracks) {
            if (song.getTrack() <= 0 || song.getTrack() > songs.size()) {
                return false;
            }
        }
        return true;
    }

    private boolean areTracksUnique() {
        Set<Integer> tracks = new HashSet<>();
        for (Song songsWithTrack : songsWithTracks) {
            if (!tracks.add(songsWithTrack.getTrack())) {
                return false;
            }
        }
        return true;
    }

    private void separateSongsByTrack() {
        for (Song song : songs) {
            // Has track read from the file
            if (song.getTrack() != -1) {
                songsWithTracks.add(song);
            } else {
                songsWithoutTracks.add(song);
            }
        }
    }

    private Song getSongWithTrack(List<Song> songs, int track) {
        return songs.stream().filter(song -> song.getTrack() == track).findFirst().orElse(null);
    }

    private int compareFileNames(Song o1, Song o2) {
        return MP3FileUtils.compareFileNames(o2.getFileName(), o1.getFileName());
    }
}
