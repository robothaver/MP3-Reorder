package com.robothaver.mp3reorder.mp3_viewer.song.track;

import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import com.robothaver.mp3reorder.mp3_viewer.song.domain.TrackAssignerResult;
import com.robothaver.mp3reorder.mp3_viewer.utils.MP3FileUtils;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TrackAssignerImpl implements TrackAssigner {
    private final List<Song> songsWithTracks = new ArrayList<>();
    private final List<Song> songsWithoutTracks = new ArrayList<>();
    private final List<Song> songs;

    @Override
    public TrackAssignerResult assignTracks() {
        boolean existingTracksAreValid = existingTracksAreValid();
        List<Song> processedSongs;
        if (existingTracksAreValid) {
            processedSongs = assignTracksWithExisting(songs);
        } else {
            processedSongs = assignNewTracks(songs);
        }
        return new TrackAssignerResult(processedSongs, existingTracksAreValid);
    }

    private boolean existingTracksAreValid() {
        for (Song song : songs) {
            // Has track read from the file
            if (song.getTrack() != -1) {
                Song duplicateSong = songsWithTracks.stream().filter(song1 -> song1.getTrack() == song.getTrack()).findFirst().orElse(null);
                // A song with this track already exists
                if (duplicateSong != null) {
                    return false;
                } else {
                    songsWithTracks.add(song);
                }
            } else {
                songsWithoutTracks.add(song);
            }
        }
        return true;
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

    private Song getSongWithTrack(List<Song> songs, int track) {
        return songs.stream().filter(song -> song.getTrack() == track).findFirst().orElse(null);
    }

    private int compareFileNames(Song o1, Song o2) {
        return MP3FileUtils.compareFileNames(o2.getFileName(), o1.getFileName());
    }
}
