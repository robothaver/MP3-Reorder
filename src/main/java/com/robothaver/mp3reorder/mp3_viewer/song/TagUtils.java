package com.robothaver.mp3reorder.mp3_viewer.song;

import com.mpatric.mp3agic.*;
import com.robothaver.mp3reorder.mp3_viewer.domain.Song;

public class TagUtils {
    private TagUtils() {
    }

    public static void readDataFromTag(Song song) {
        if (song.getTag() == null || song.isReadDataFromTag()) return;
        ID3v2 tag = song.getTag();
        song.artistProperty().set(tag.getArtist());
        song.albumProperty().set(tag.getAlbum());
        song.yearProperty().set(tag.getYear());
        song.genreProperty().set(tag.getGenre());
        song.genreDescriptionProperty().set(String.valueOf(tag.getGenreDescription()));
        song.commentProperty().set(tag.getComment());
        song.lyricsProperty().set(tag.getLyrics());
        song.composerProperty().set(tag.getComposer());
        song.publisherProperty().set(tag.getPublisher());
        song.originalArtistProperty().set(tag.getOriginalArtist());
        song.albumArtistProperty().set(tag.getAlbumArtist());
        song.copyrightProperty().set(tag.getCopyright());
        song.urlProperty().set(tag.getUrl());
        song.encoderProperty().set(tag.getEncoder());
        byte[] imageData = tag.getAlbumImage();
        if (imageData != null) {
            song.setAlbumImage(imageData);
        }
        song.setReadDataFromTag(true);
    }

    public static void writeDataToTag(Song song) {
        ID3v2 tag = getOrCreateTag(song);
        tag.setArtist(song.artistProperty().get());
        tag.setAlbum(song.albumProperty().get());
        tag.setYear(song.yearProperty().get());
        Integer genre = song.genreProperty().getValue();
        String genreDescription = song.genreDescriptionProperty().get();
        boolean genreDescriptionValid = ID3v1Genres.matchGenreDescription(genreDescription) != -1;
        if (genreDescriptionValid) {
            tag.setGenre(genre);
            tag.setGenreDescription(genreDescription);
        }
        tag.setComment(song.commentProperty().get());
        tag.setLyrics(song.lyricsProperty().get());
        tag.setComposer(song.composerProperty().get());
        tag.setPublisher(song.publisherProperty().get());
        tag.setOriginalArtist(song.originalArtistProperty().get());
        tag.setAlbumArtist(song.albumArtistProperty().get());
        tag.setCopyright(song.copyrightProperty().get());
        tag.setUrl(song.urlProperty().get());
        tag.setEncoder(song.encoderProperty().get());
        tag.setTrack(String.valueOf(song.getTrack()));
        tag.setTitle(song.titleProperty().get());
    }

    private static ID3v2 getOrCreateTag(Song song) {
        ID3v2 tag = song.getTag();
        if (tag == null) {
            tag = new ID3v24Tag();
            song.setTag(tag);
            song.getMp3File().setId3v2Tag(tag);
        }
        return tag;
    }
}
