package com.robothaver.mp3reorder.core.preference;

import com.robothaver.mp3reorder.mp3.controls.menubar.Themes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Preferences {
    private boolean sideMenuEnabled;
    private boolean statusBarEnabled;
    private boolean launchMaximized;
    private boolean useSystemMenuBar;
    private Themes selectedTheme;
    private int selectedSize;
    private Locale selectedLocale;
}
