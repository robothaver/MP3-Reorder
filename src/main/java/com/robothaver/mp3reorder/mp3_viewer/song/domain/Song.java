package com.robothaver.mp3reorder.mp3_viewer.song.domain;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.Mp3File;
import javafx.beans.property.*;

import java.nio.file.Path;

public class Song {
    private final ObjectProperty<Integer> track = new SimpleObjectProperty<>();
    private final StringProperty fileName = new SimpleStringProperty();
    private final Mp3File mp3File;
    private final Path path;
    private final ObjectProperty<Integer> genre = new SimpleObjectProperty<>();
    private final StringProperty artist = new SimpleStringProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty album = new SimpleStringProperty();
    private final StringProperty year = new SimpleStringProperty();
    private final StringProperty genreDescription = new SimpleStringProperty();
    private final StringProperty comment = new SimpleStringProperty();
    private final StringProperty lyrics = new SimpleStringProperty();
    private final StringProperty composer = new SimpleStringProperty();
    private final StringProperty publisher = new SimpleStringProperty();
    private final StringProperty originalArtist = new SimpleStringProperty();
    private final StringProperty albumArtist = new SimpleStringProperty();
    private final StringProperty copyright = new SimpleStringProperty();
    private final StringProperty url = new SimpleStringProperty();
    private final StringProperty encoder = new SimpleStringProperty();
    private ID3v2 tag;
    private byte[] albumImage;

    public Song(Mp3File mp3File, Path path) {
        this.mp3File = mp3File;
        this.path = path;
        init();
    }

    public String getGenreDescription() {
        return genreDescription.get();
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

    public ObjectProperty<Integer> trackProperty() {
        return track;
    }

    public String getFileName() {
        return fileName.get();
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }

    public Mp3File getMp3File() {
        return mp3File;
    }

    public Path getPath() {
        return path;
    }

    public ID3v2 getTag() {
        return tag;
    }

    public String getArtist() {
        return artist.get();
    }

    public StringProperty artistProperty() {
        return artist;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getAlbum() {
        return album.get();
    }

    public StringProperty albumProperty() {
        return album;
    }

    public String getYear() {
        return year.get();
    }

    public StringProperty yearProperty() {
        return year;
    }

    public int getGenre() {
        return genre.get();
    }

    public Property<Integer> genreProperty() {
        return genre;
    }

    public String getComment() {
        return comment.get();
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public String getLyrics() {
        return lyrics.get();
    }

    public StringProperty lyricsProperty() {
        return lyrics;
    }

    public String getComposer() {
        return composer.get();
    }

    public StringProperty composerProperty() {
        return composer;
    }

    public String getPublisher() {
        return publisher.get();
    }

    public StringProperty publisherProperty() {
        return publisher;
    }

    public String getOriginalArtist() {
        return originalArtist.get();
    }

    public StringProperty originalArtistProperty() {
        return originalArtist;
    }

    public String getAlbumArtist() {
        return albumArtist.get();
    }

    public StringProperty albumArtistProperty() {
        return albumArtist;
    }

    public String getCopyright() {
        return copyright.get();
    }

    public StringProperty copyrightProperty() {
        return copyright;
    }

    public String getUrl() {
        return url.get();
    }

    public StringProperty urlProperty() {
        return url;
    }

    public String getEncoder() {
        return encoder.get();
    }

    public StringProperty encoderProperty() {
        return encoder;
    }

    public byte[] getAlbumImage() {
        return albumImage;
    }

    public void init() {
        if (tag != null) return;

        if (mp3File.hasId3v2Tag()) {
            tag = mp3File.getId3v2Tag();
            loadDataFromTag();
        } else {
            tag = new ID3v24Tag();
            mp3File.setId3v2Tag(tag);
        }
    }

    private void loadDataFromTag() {
        int trackNumber;
        try {
            trackNumber = Integer.parseInt(tag.getTrack());
        } catch (NumberFormatException e) {
            trackNumber = -1;
        }
        track.set(trackNumber);
        artist.set(tag.getArtist());
        title.set(tag.getTitle());
        album.set(tag.getAlbum());
        year.set(tag.getYear());
        genre.set(tag.getGenre());
        genreDescription.set(tag.getGenreDescription());
        comment.set(tag.getComment());
        lyrics.set(tag.getLyrics());
        composer.set(tag.getComposer());
        publisher.set(tag.getPublisher());
        originalArtist.set(tag.getOriginalArtist());
        albumArtist.set(tag.getAlbumArtist());
        copyright.set(tag.getCopyright());
        url.set(tag.getUrl());
        encoder.set(tag.getEncoder());
        byte[] imageData = tag.getAlbumImage();
        if (imageData != null) {
            albumImage = imageData;
        }
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
