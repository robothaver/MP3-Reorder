package com.robothaver.mp3reordergradle.mp3_viewer.song.domain;

import lombok.Data;

import java.util.List;

@Data
public class TrackAssignerResult {
    private final List<Song> songs;
    private final boolean usedExistingTracks;
}
