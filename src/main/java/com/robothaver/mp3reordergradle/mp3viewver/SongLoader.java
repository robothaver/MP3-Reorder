package com.robothaver.mp3reordergradle.mp3viewver;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.concurrent.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class SongLoader extends Task<List<Song>> {
    private final String selectedPath;

    public SongLoader(String selectedPath) {
        this.selectedPath = selectedPath;
    }

    @Override
    protected List<Song> call() {
        System.out.println("Loading songs from " + selectedPath);
        Path path = Paths.get(selectedPath);
        try (Stream<Path> stream = Files.list(path)) {
            List<Song> songs = stream
                    .filter(file -> !file.endsWith(".mp3"))
                    .map(file -> {
                            try {
                                return createSongFromFile(file);
                            } catch (IOException | UnsupportedTagException | InvalidDataException e) {
                                System.out.println("Failed to load songs: " + e);
                                throw new IllegalStateException(e);
                            }
                        }
                    )
                    .sorted()
                    .toList();
            assignTrackNumbers(songs);
            System.out.println("Loaded " + songs.size() + " mp3 files");
            return songs.stream().sorted(Comparator.comparingInt(Song::getTrack)).toList();
        } catch (IOException e) {
            System.out.println("Selected path not found!");
            throw new IllegalStateException(e);
        }
    }

    private void assignTrackNumbers(List<Song> songs) {
        for (int i = 0; i < songs.size(); i++) {
            Song song = songs.get(i);
            if (song.getTrack() == -1) {
                song.setTrack(i + 1);
            }
        }
    }

    private Song createSongFromFile(Path file) throws InvalidDataException, UnsupportedTagException, IOException {
        Mp3File mp3File = new Mp3File(file);
        Song song = new Song(mp3File, file);
        int trackNumber = -1;
        if (mp3File.hasId3v2Tag()) {
            song.titleProperty().setValue(mp3File.getId3v2Tag().getTitle());
            trackNumber = getTrackFromTag(mp3File);
        }
        song.trackProperty().setValue(trackNumber);
        song.fileNameProperty().setValue(file.getFileName().toString());
        return song;
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
