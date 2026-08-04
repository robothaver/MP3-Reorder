package com.robothaver.mp3reorder.mp3;

import com.robothaver.mp3reorder.core.preference.PreferenceStoreImpl;
import com.robothaver.mp3reorder.mp3.domain.Song;
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
}
