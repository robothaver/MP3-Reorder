package com.robothaver.mp3reorder.mp3.song.track.editor;

public interface MP3TrackEditor {
    void setNewTrackForSong(int currentTrack, int newTrack);
    void insertSong(int currentSongIndex, int newSongIndex);
    void swapSongsAndTracks(int index1, int index2);
}
