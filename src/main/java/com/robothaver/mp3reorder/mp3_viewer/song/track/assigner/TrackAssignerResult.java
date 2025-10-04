package com.robothaver.mp3reorder.mp3_viewer.song.track.assigner;

import com.robothaver.mp3reorder.mp3_viewer.song.domain.Song;
import lombok.Data;

import java.util.List;

@Data
public class TrackAssignerResult {
    private final List<Song> songs;
    private final TrackIssue trackIssue;
}
