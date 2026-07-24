package com.robothaver.mp3reorder.mp3;

import com.robothaver.mp3reorder.core.preference.PreferenceStoreImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MP3Interactor {
    private final MP3Model mp3Model;

    public void closeDetailsSideMenu() {
        mp3Model.getMenuBarModel().getDetailsMenuEnabled().set(false);
        PreferenceStoreImpl.getInstance().getPreferences().setSideMenuEnabled(false);
        PreferenceStoreImpl.getInstance().savePreferences();
    }
}
