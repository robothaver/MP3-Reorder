package com.robothaver.mp3reorder.mp3;

import com.robothaver.mp3reorder.core.preference.PreferenceStoreImpl;
import com.robothaver.mp3reorder.mp3.domain.Song;
import com.robothaver.mp3reorder.mp3.song.TagUtils;
import javafx.collections.ObservableList;

public class MP3Interactor {
    private final MP3Model mp3Model;

    public MP3Interactor(MP3Model mp3Model) {
        this.mp3Model = mp3Model;

        mp3Model.selectedSongIndexProperty().addListener((_, _, newValue) -> {
            if (newValue.intValue() != -1) {
                Song song = mp3Model.getSongs().get(newValue.intValue());
                TagUtils.readDataFromTag(song);
                mp3Model.setSelectedSong(song);
            } else {
                mp3Model.setSelectedSong(null);
            }
        });
    }

    public void closeDetailsSideMenu() {
        mp3Model.getMenuBarModel().getDetailsMenuEnabled().set(false);
        PreferenceStoreImpl.getInstance().getPreferences().setSideMenuEnabled(false);
        PreferenceStoreImpl.getInstance().savePreferences();
    }

    public void togglePlay(int index) {
        mp3Model.setSelectedSongIndex(index);
        Song newSong = mp3Model.getSelectedSong();
        if (mp3Model.getSongInPlayer() != null && newSong.equals(mp3Model.getSongInPlayer())) {
            mp3Model.setSongPlaying(!mp3Model.isSongPlaying());
        } else {
            mp3Model.setSongInPlayer(newSong);
        }
    }

    public void onPlayNext() {
        ObservableList<Song> songs = mp3Model.getSongs();
        if (songs.size() < 2) return;

        Song songInPlayer = mp3Model.getSongInPlayer();
        if (songInPlayer != null) {
            int nextIndex = songs.indexOf(songInPlayer) + 1;
            if (nextIndex == songs.size()) nextIndex = 0;
            mp3Model.setSelectedSongIndex(nextIndex);
            mp3Model.setSongInPlayer(songs.get(nextIndex));
        }
    }

    public void onPlayPrevious() {
        ObservableList<Song> songs = mp3Model.getSongs();
        if (songs.size() < 2) return;

        Song songInPlayer = mp3Model.getSongInPlayer();
        if (songInPlayer != null) {
            int previousIndex = songs.indexOf(songInPlayer) - 1;
            if (previousIndex == -1) previousIndex = songs.size() - 1;
            mp3Model.setSelectedSongIndex(previousIndex);
            mp3Model.setSongInPlayer(songs.get(previousIndex));
        }
    }

    public void onScrollToPlaying() {
        Song songInPlayer = mp3Model.getSongInPlayer();
        if (songInPlayer != null) {
            mp3Model.setSelectedSongIndex(mp3Model.getSongs().indexOf(songInPlayer));
            mp3Model.setScrollToSelected(!mp3Model.isScrollToSelected());
        }
    }

    public void onPlayPressedWhenEmpty() {
        int index = mp3Model.getSelectedSongIndex();
        if (index == -1) return;

        mp3Model.setSongInPlayer(mp3Model.getSongs().get(index));
    }
}
