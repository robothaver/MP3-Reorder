package com.robothaver.mp3reorder.mp3_viewer.song.domain;

import lombok.Data;

import java.util.List;

@Data
public class TrackAssignerResult {
    private final List<Song> songs;
    private final boolean usedExistingTracks;
}
