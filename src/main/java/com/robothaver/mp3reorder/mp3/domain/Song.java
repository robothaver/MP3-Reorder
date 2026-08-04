package com.robothaver.mp3reorder.mp3.domain;

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
    @Getter
    @Setter
    private boolean readDataFromTag = false;

    public Song(Mp3File mp3File, Path path, int track, ID3v2 tag) {
        this(mp3File, path, track, tag.getTitle(), tag);
    }

    public Song(Mp3File mp3File, Path path) {
        this(mp3File, path, -1, "", null);
    }

    private Song(Mp3File mp3File, Path path, int track, String title, ID3v2 tag) {
        this.mp3File = mp3File;
        this.path = path;
        this.track.set(track);
        this.title.set(title);
        this.fileName.set(path.getFileName().toString());
        this.tag = tag;
    }

    public boolean isFileChanged() {
        return fileChanged.get();
    }

    public BooleanProperty fileChangedProperty() {
        return fileChanged;
    }

    public void setFileChanged(boolean fileChanged) {
        this.fileChanged.set(fileChanged);
    }

    public int getTrack() {
        return track.get();
    }

    public TrackedIntegerProperty trackProperty() {
        return track;
    }

    public void setTrack(int track) {
        this.track.set(track);
    }

    public String getFileName() {
        return fileName.get();
    }

    public TrackedStringProperty fileNameProperty() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    public String getTitle() {
        return title.get();
    }

    public TrackedStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public int getGenre() {
        return genre.get();
    }

    public TrackedIntegerProperty genreProperty() {
        return genre;
    }

    public void setGenre(int genre) {
        this.genre.set(genre);
    }

    public String getArtist() {
        return artist.get();
    }

    public TrackedStringProperty artistProperty() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist.set(artist);
    }

    public String getAlbum() {
        return album.get();
    }

    public TrackedStringProperty albumProperty() {
        return album;
    }

    public void setAlbum(String album) {
        this.album.set(album);
    }

    public String getYear() {
        return year.get();
    }

    public TrackedStringProperty yearProperty() {
        return year;
    }

    public void setYear(String year) {
        this.year.set(year);
    }

    public String getGenreDescription() {
        return genreDescription.get();
    }

    public TrackedStringProperty genreDescriptionProperty() {
        return genreDescription;
    }

    public void setGenreDescription(String genreDescription) {
        this.genreDescription.set(genreDescription);
    }

    public String getComment() {
        return comment.get();
    }

    public TrackedStringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public String getLyrics() {
        return lyrics.get();
    }

    public TrackedStringProperty lyricsProperty() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics.set(lyrics);
    }

    public String getComposer() {
        return composer.get();
    }

    public TrackedStringProperty composerProperty() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer.set(composer);
    }

    public String getPublisher() {
        return publisher.get();
    }

    public TrackedStringProperty publisherProperty() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    public String getOriginalArtist() {
        return originalArtist.get();
    }

    public TrackedStringProperty originalArtistProperty() {
        return originalArtist;
    }

    public void setOriginalArtist(String originalArtist) {
        this.originalArtist.set(originalArtist);
    }

    public String getAlbumArtist() {
        return albumArtist.get();
    }

    public TrackedStringProperty albumArtistProperty() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist.set(albumArtist);
    }

    public String getCopyright() {
        return copyright.get();
    }

    public TrackedStringProperty copyrightProperty() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright.set(copyright);
    }

    public String getUrl() {
        return url.get();
    }

    public TrackedStringProperty urlProperty() {
        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public String getEncoder() {
        return encoder.get();
    }

    public TrackedStringProperty encoderProperty() {
        return encoder;
    }

    public void setEncoder(String encoder) {
        this.encoder.set(encoder);
    }

    @Override
    public String toString() {
        return "Song{" + "track=" + track.getValue() + ", fileName=" + fileName.getValue() + ", title=" + title.getValue() + '}';
    }
}
