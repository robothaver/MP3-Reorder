package com.robothaver.mp3reorder.mp3;

import com.robothaver.mp3reorder.core.preference.PreferenceStoreImpl;
import com.robothaver.mp3reorder.mp3.domain.Song;
import javafx.collections.ObservableList;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MP3Interactor {
    private final MP3Model mp3Model;

    public void closeDetailsSideMenu() {
        mp3Model.getMenuBarModel().getDetailsMenuEnabled().set(false);
        PreferenceStoreImpl.getInstance().getPreferences().setSideMenuEnabled(false);
        PreferenceStoreImpl.getInstance().savePreferences();
    }

    public void togglePlay(int index) {
        Song newSong = mp3Model.getSongs().get(index);
        if (mp3Model.getSongInPlayer() != null && newSong.equals(mp3Model.getSongInPlayer())) {
            mp3Model.setSongPlaying(!mp3Model.isSongPlaying());
        } else {
            mp3Model.setSongInPlayer(newSong);
            mp3Model.setSongPlaying(true);
        }
    }

    public void onPlayNext() {
        ObservableList<Song> songs = mp3Model.getSongs();
        if (songs.size() < 2) return;

        Song songInPlayer = mp3Model.getSongInPlayer();
        if (songInPlayer != null) {
            int nextIndex = songs.indexOf(songInPlayer) + 1;
            if (nextIndex == songs.size()) nextIndex = 0;
            mp3Model.setSongInPlayer(songs.get(nextIndex));
            mp3Model.setSelectedSongIndex(nextIndex);
            mp3Model.setSongPlaying(true);
        }
    }

    public void onPlayPrevious() {
        ObservableList<Song> songs = mp3Model.getSongs();
        if (songs.size() < 2) return;

        Song songInPlayer = mp3Model.getSongInPlayer();
        if (songInPlayer != null) {
            int previousIndex = songs.indexOf(songInPlayer) - 1;
            if (previousIndex == -1) previousIndex = songs.size() - 1;
            mp3Model.setSongInPlayer(songs.get(previousIndex));
            mp3Model.setSelectedSongIndex(previousIndex);
            mp3Model.setSongPlaying(true);
        }
    }

    public void onScrollToPlaying() {
        Song songInPlayer = mp3Model.getSongInPlayer();
        if (songInPlayer != null) {
            mp3Model.setSelectedSongIndex(mp3Model.getSongs().indexOf(songInPlayer));
            mp3Model.setScrollToSelected(!mp3Model.getScrollToSelected());
        }
    }
}
