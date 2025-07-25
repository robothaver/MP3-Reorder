package com.robothaver.mp3reordergradle.mp3viewver;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.Mp3File;
import com.robothaver.mp3reordergradle.mp3viewver.utils.MP3FileUtils;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.nio.file.Path;

public class Song implements Comparable<Song> {
    private final IntegerProperty track = new SimpleIntegerProperty();
    private final StringProperty fileName = new SimpleStringProperty();
    private final Mp3File mp3File;
    private final Path path;
    private ID3v2 tag;

    private final StringProperty artist = new SimpleStringProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty album = new SimpleStringProperty();
    private final StringProperty year = new SimpleStringProperty();
    private final IntegerProperty genre = new SimpleIntegerProperty();
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
    private byte[] albumImage;

    public Song(Mp3File mp3File, Path path) {
        this.mp3File = mp3File;
        this.path = path;
        init();
    }

    public int getTrack() {
        return track.get();
    }

    public IntegerProperty trackProperty() {
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

    public void setTag(ID3v2 tag) {
        this.tag = tag;
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

    public IntegerProperty genreProperty() {
        return genre;
    }

    public String getGenreDescription() {
        return genreDescription.get();
    }

    public StringProperty genreDescriptionProperty() {
        return genreDescription;
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

    public void setTrack(int value) {
        track.setValue(value);
        tag.setTrack(String.valueOf(value));
    }
    
    private void init() {
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

    @Override
    public int compareTo(Song o) {
        return MP3FileUtils.compareFileNames(getFileName(), o.getFileName());
    }
}
