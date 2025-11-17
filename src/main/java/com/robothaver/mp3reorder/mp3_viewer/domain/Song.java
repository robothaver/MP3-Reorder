package com.robothaver.mp3reorder.mp3_viewer.domain;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

public class Song {
    @Getter
    @Setter
    private BooleanProperty fileChanged = new SimpleBooleanProperty(false);
    private final TrackedIntegerProperty track = new TrackedIntegerProperty(fileChanged);
    private final TrackedStringProperty fileName = new TrackedStringProperty(fileChanged);
    private final TrackedStringProperty title = new TrackedStringProperty(fileChanged);
    private final TrackedIntegerProperty genre = new TrackedIntegerProperty(fileChanged);
    private final TrackedStringProperty artist = new TrackedStringProperty(fileChanged);
    private final TrackedStringProperty album = new TrackedStringProperty(fileChanged);
    private final TrackedStringProperty year = new TrackedStringProperty(fileChanged);
    private final TrackedStringProperty genreDescription = new TrackedStringProperty(fileChanged);
    private final TrackedStringProperty comment = new TrackedStringProperty(fileChanged);
    private final TrackedStringProperty lyrics = new TrackedStringProperty(fileChanged);
    private final TrackedStringProperty composer = new TrackedStringProperty(fileChanged);
    private final TrackedStringProperty publisher = new TrackedStringProperty(fileChanged);
    private final TrackedStringProperty originalArtist = new TrackedStringProperty(fileChanged);
    private final TrackedStringProperty albumArtist = new TrackedStringProperty(fileChanged);
    private final TrackedStringProperty copyright = new TrackedStringProperty(fileChanged);
    private final TrackedStringProperty url = new TrackedStringProperty(fileChanged);
    private final TrackedStringProperty encoder = new TrackedStringProperty(fileChanged);
    @Getter
    @Setter
    private byte[] albumImage;
    @Getter
    @Setter
    private Mp3File mp3File;
    @Getter
    @Setter
    private Path path;
    @Getter
    @Setter
    private ID3v2 tag;

    public Song(Mp3File mp3File, Path path, int track, String title) {
        this.mp3File = mp3File;
        this.path = path;
        this.track.set(track);
        this.title.set(title);
        this.fileName.set(path.getFileName().toString());
    }

    public int getTrack() {
        return track.get();
    }

    public TrackedIntegerProperty trackProperty() {
        return track;
    }

    public String getFileName() {
        return fileName.get();
    }

    public TrackedStringProperty fileNameProperty() {
        return fileName;
    }

    public String getTitle() {
        return title.get();
    }

    public TrackedStringProperty titleProperty() {
        return title;
    }

    public TrackedIntegerProperty genreProperty() {
        return genre;
    }

    public TrackedStringProperty artistProperty() {
        return artist;
    }

    public TrackedStringProperty albumProperty() {
        return album;
    }

    public TrackedStringProperty yearProperty() {
        return year;
    }

    public TrackedStringProperty genreDescriptionProperty() {
        return genreDescription;
    }

    public TrackedStringProperty commentProperty() {
        return comment;
    }

    public TrackedStringProperty lyricsProperty() {
        return lyrics;
    }

    public TrackedStringProperty composerProperty() {
        return composer;
    }

    public TrackedStringProperty publisherProperty() {
        return publisher;
    }

    public TrackedStringProperty originalArtistProperty() {
        return originalArtist;
    }

    public TrackedStringProperty albumArtistProperty() {
        return albumArtist;
    }

    public TrackedStringProperty copyrightProperty() {
        return copyright;
    }

    public TrackedStringProperty urlProperty() {
        return url;
    }

    public TrackedStringProperty encoderProperty() {
        return encoder;
    }

    @Override
    public String toString() {
        return "Song{" + "track=" + track.getValue() + ", fileName=" + fileName.getValue() + ", title=" + title.getValue() + '}';
    }
}
