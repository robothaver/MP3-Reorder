package com.robothaver.mp3reorder.mp3.song;

import com.mpatric.mp3agic.*;
import com.robothaver.mp3reorder.mp3.domain.Song;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Path;

@Log4j2
public class TagUtils {

    private TagUtils() {
        /* This utility class should not be instantiated */
    }

    public static void readDataFromTag(Song song) {
        if (song.isReadDataFromTag()) return;
        ID3v2 tag = readOnlyTagFromFile(song.getPath());
        if (tag == null) return;

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

    public static void writeDataToTag(Song song, Mp3File mp3File) {
        ID3v2 tag = getOrCreateTag(mp3File);
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

    private static ID3v2 readOnlyTagFromFile(Path path) {
        try {
            return new Mp3File(path, 65536, false).getId3v2Tag();
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            log.error("Failed to read tag from song", e);
        }
        return null;
    }

    private static ID3v2 getOrCreateTag(Mp3File mp3File) {
        if (mp3File.hasId3v2Tag()) return mp3File.getId3v2Tag();
        ID3v24Tag tag = new ID3v24Tag();
        mp3File.setId3v2Tag(tag);
        return tag;
    }

    private static String formatString(String string) {
        if (string == null) return "";
        if (string.isBlank()) return "";
        return string.trim();
    }
}
