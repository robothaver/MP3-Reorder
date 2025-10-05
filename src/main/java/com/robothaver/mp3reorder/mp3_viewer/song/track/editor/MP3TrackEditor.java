package com.robothaver.mp3reorder.mp3_viewer.song.track.editor;

public interface MP3TrackEditor {
    void setNewTrackForSong(int currentTrack, int newTrack);
    void swapSongsAndTracks(int index1, int index2);
}
