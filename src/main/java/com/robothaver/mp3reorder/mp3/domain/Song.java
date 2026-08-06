package com.robothaver.mp3reorder.mp3.domain;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.nio.file.Path;

public class Song {
    private final BooleanProperty fileChanged = new SimpleBooleanProperty(false);
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

    private byte[] albumImage;
    private Mp3File mp3File;
    private Path path;
    private ID3v2 tag;
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

    public void setFileChanged(boolean fileChanged) {
        this.fileChanged.set(fileChanged);
    }

    public BooleanProperty fileChangedProperty() {
        return fileChanged;
    }

    public int getTrack() {
        return track.get();
    }

    public void setTrack(int track) {
        this.track.set(track);
    }

    public TrackedIntegerProperty trackProperty() {
        return track;
    }

    public String getFileName() {
        return fileName.get();
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    public TrackedStringProperty fileNameProperty() {
        return fileName;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public TrackedStringProperty titleProperty() {
        return title;
    }

    public int getGenre() {
        return genre.get();
    }

    public void setGenre(int genre) {
        this.genre.set(genre);
    }

    public TrackedIntegerProperty genreProperty() {
        return genre;
    }

    public String getArtist() {
        return artist.get();
    }

    public void setArtist(String artist) {
        this.artist.set(artist);
    }

    public TrackedStringProperty artistProperty() {
        return artist;
    }

    public String getAlbum() {
        return album.get();
    }

    public void setAlbum(String album) {
        this.album.set(album);
    }

    public TrackedStringProperty albumProperty() {
        return album;
    }

    public String getYear() {
        return year.get();
    }

    public void setYear(String year) {
        this.year.set(year);
    }

    public TrackedStringProperty yearProperty() {
        return year;
    }

    public String getGenreDescription() {
        return genreDescription.get();
    }

    public void setGenreDescription(String genreDescription) {
        this.genreDescription.set(genreDescription);
    }

    public TrackedStringProperty genreDescriptionProperty() {
        return genreDescription;
    }

    public String getComment() {
        return comment.get();
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public TrackedStringProperty commentProperty() {
        return comment;
    }

    public String getLyrics() {
        return lyrics.get();
    }

    public void setLyrics(String lyrics) {
        this.lyrics.set(lyrics);
    }

    public TrackedStringProperty lyricsProperty() {
        return lyrics;
    }

    public String getComposer() {
        return composer.get();
    }

    public void setComposer(String composer) {
        this.composer.set(composer);
    }

    public TrackedStringProperty composerProperty() {
        return composer;
    }

    public String getPublisher() {
        return publisher.get();
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    public TrackedStringProperty publisherProperty() {
        return publisher;
    }

    public String getOriginalArtist() {
        return originalArtist.get();
    }

    public void setOriginalArtist(String originalArtist) {
        this.originalArtist.set(originalArtist);
    }

    public TrackedStringProperty originalArtistProperty() {
        return originalArtist;
    }

    public String getAlbumArtist() {
        return albumArtist.get();
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist.set(albumArtist);
    }

    public TrackedStringProperty albumArtistProperty() {
        return albumArtist;
    }

    public String getCopyright() {
        return copyright.get();
    }

    public void setCopyright(String copyright) {
        this.copyright.set(copyright);
    }

    public TrackedStringProperty copyrightProperty() {
        return copyright;
    }

    public String getUrl() {
        return url.get();
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public TrackedStringProperty urlProperty() {
        return url;
    }

    public String getEncoder() {
        return encoder.get();
    }

    public void setEncoder(String encoder) {
        this.encoder.set(encoder);
    }

    public TrackedStringProperty encoderProperty() {
        return encoder;
    }

    public byte[] getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(byte[] albumImage) {
        this.albumImage = albumImage;
    }

    public Mp3File getMp3File() {
        return mp3File;
    }

    public void setMp3File(Mp3File mp3File) {
        this.mp3File = mp3File;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public ID3v2 getTag() {
        return tag;
    }

    public void setTag(ID3v2 tag) {
        this.tag = tag;
    }

    public boolean isReadDataFromTag() {
        return readDataFromTag;
    }

    public void setReadDataFromTag(boolean readDataFromTag) {
        this.readDataFromTag = readDataFromTag;
    }

    @Override
    public String toString() {
        return "Song{" + "track=" + track.getValue() + ", fileName=" + fileName.getValue() + ", title=" + title.getValue() + '}';
    }
}
