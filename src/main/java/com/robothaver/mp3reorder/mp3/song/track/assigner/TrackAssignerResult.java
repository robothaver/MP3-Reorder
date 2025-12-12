package com.robothaver.mp3reorder.mp3.song.track.assigner;

import com.robothaver.mp3reorder.mp3.domain.Song;
import lombok.Data;

import java.util.List;

@Data
public class TrackAssignerResult {
    private final List<Song> songs;
    private final TrackIssue trackIssue;
}
