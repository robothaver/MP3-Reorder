package com.robothaver.mp3reorder.mp3_viewer.song.domain;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.Mp3File;
import javafx.beans.property.*;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

public class Song {
    private final IntegerProperty track = new SimpleIntegerProperty();
    private final StringProperty fileName = new SimpleStringProperty();
    @Getter
    private final Mp3File mp3File;
    @Getter
    private final Path path;

    private final IntegerProperty genre = new SimpleIntegerProperty(-1);
    private final StringProperty artist = new SimpleStringProperty("");
    private final StringProperty title = new SimpleStringProperty("");
    private final StringProperty album = new SimpleStringProperty("");
    private final StringProperty year = new SimpleStringProperty("");
    private final StringProperty genreDescription = new SimpleStringProperty("");
    private final StringProperty comment = new SimpleStringProperty("");
    private final StringProperty lyrics = new SimpleStringProperty("");
    private final StringProperty composer = new SimpleStringProperty("");
    private final StringProperty publisher = new SimpleStringProperty("");
    private final StringProperty originalArtist = new SimpleStringProperty("");
    private final StringProperty albumArtist = new SimpleStringProperty("");
    private final StringProperty copyright = new SimpleStringProperty("");
    private final StringProperty url = new SimpleStringProperty("");
    private final StringProperty encoder = new SimpleStringProperty("");
    @Getter
    @Setter
    private ID3v2 tag;
    @Getter
    @Setter
    private byte[] albumImage;

    public Song(Mp3File mp3File, Path path) {
        this.mp3File = mp3File;
        this.path = path;
    }

    public StringProperty genreDescriptionProperty() {
        return genreDescription;
    }

    public int getTrack() {
        return track.get();
    }

    public void setTrack(int value) {
        track.setValue(value);
    }

    public IntegerProperty trackProperty() { return track; }

    public String getFileName() { return fileName.get(); }

    public StringProperty fileNameProperty() {
        return fileName;
    }

    public StringProperty artistProperty() {
        return artist;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty albumProperty() {
        return album;
    }

    public StringProperty yearProperty() {
        return year;
    }

    public IntegerProperty genreProperty() { return genre; }

    public StringProperty commentProperty() {
        return comment;
    }

    public StringProperty lyricsProperty() {
        return lyrics;
    }

    public StringProperty composerProperty() {
        return composer;
    }

    public StringProperty publisherProperty() {
        return publisher;
    }

    public StringProperty originalArtistProperty() {
        return originalArtist;
    }

    public StringProperty albumArtistProperty() {
        return albumArtist;
    }

    public StringProperty copyrightProperty() {
        return copyright;
    }

    public StringProperty urlProperty() {
        return url;
    }

    public StringProperty encoderProperty() {
        return encoder;
    }

    @Override
    public String toString() {
        return "Song{" +
                "track=" + track.getValue() +
                ", fileName=" + fileName.getValue() +
                ", title=" + title.getValue() +
                '}';
    }
}
