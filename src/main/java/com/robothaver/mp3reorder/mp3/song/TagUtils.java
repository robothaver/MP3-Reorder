package com.robothaver.mp3reorder.mp3.song;

import com.mpatric.mp3agic.*;
import com.robothaver.mp3reorder.mp3.domain.Song;

public class TagUtils {
    private TagUtils() {
    }

    public static void readDataFromTag(Song song) {
        if (song.getTag() == null || song.isReadDataFromTag()) return;
        ID3v2 tag = song.getTag();
        song.artistProperty().set(formatString(tag.getArtist()));
        song.albumProperty().set(formatString(tag.getAlbum()));
        song.yearProperty().set(formatString(tag.getYear()));
        song.genreProperty().set(tag.getGenre());
        song.genreDescriptionProperty().set(String.valueOf(tag.getGenreDescription()));
        song.commentProperty().set(formatString(tag.getComment()));
        song.lyricsProperty().set(formatString(tag.getLyrics()));
        song.composerProperty().set(formatString(tag.getComposer()));
        song.publisherProperty().set(formatString(tag.getPublisher()));
        song.originalArtistProperty().set(formatString(tag.getOriginalArtist()));
        song.albumArtistProperty().set(formatString(tag.getAlbumArtist()));
        song.copyrightProperty().set(formatString(tag.getCopyright()));
        song.urlProperty().set(formatString(tag.getUrl()));
        song.encoderProperty().set(formatString(tag.getEncoder()));
        byte[] imageData = tag.getAlbumImage();
        if (imageData != null) {
            song.setAlbumImage(imageData);
        }
        song.setReadDataFromTag(true);
    }

    public static void writeDataToTag(Song song) {
        ID3v2 tag = getOrCreateTag(song);
        tag.setArtist(formatString(song.artistProperty().get()));
        tag.setAlbum(formatString(song.albumProperty().get()));
        tag.setYear(formatString(song.yearProperty().get()));
        Integer genre = song.genreProperty().getValue();
        String genreDescription = song.genreDescriptionProperty().get();
        boolean genreDescriptionValid = ID3v1Genres.matchGenreDescription(genreDescription) != -1;
        if (genreDescriptionValid) {
            tag.setGenre(genre);
            tag.setGenreDescription(genreDescription);
        }
        tag.setComment(formatString(song.commentProperty().get()));
        tag.setLyrics(formatString(song.lyricsProperty().get()));
        tag.setComposer(formatString(song.composerProperty().get()));
        tag.setPublisher(formatString(song.publisherProperty().get()));
        tag.setOriginalArtist(formatString(song.originalArtistProperty().get()));
        tag.setAlbumArtist(formatString(song.albumArtistProperty().get()));
        tag.setCopyright(formatString(song.copyrightProperty().get()));
        tag.setUrl(formatString(song.urlProperty().get()));
        tag.setEncoder(formatString(song.encoderProperty().get()));
        tag.setTrack(String.valueOf(song.getTrack()));
        tag.setTitle(formatString(song.titleProperty().get()));
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

    private static String formatString(String string) {
        if (string == null) return "";
        if (string.isBlank()) return "";
        return string.trim();
    }
}
