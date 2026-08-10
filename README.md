<div align="center">
  <img src="src/main/resources/images/logo.png" width="256" alt="MP3 Reorder Logo">
  <h1>MP3 Reorder</h1>

  <p><i>A cross-platform desktop application designed to reorder MP3 files using their track field.</i></p>

  <img src="https://img.shields.io/badge/Java-25-orange.svg" alt="Java 25">
  <img src="https://img.shields.io/badge/Platform-Windows%20%7C%20macOS%20%7C%20Linux-lightgrey.svg" alt="Platforms">
  <img src="https://img.shields.io/badge/UI-JavaFX-blue.svg" alt="JavaFX">
</div>

---

## What is MP3 Reorder?

MP3 Reorder is a desktop application designed to help you organize your local music library. The core focus of the app
is bulk-editing the MP3 `track` field, but it also functions as a general ID3 tag editor for modifying titles, artists,
genres, and more.

## How it works

The application enforces track numbering. It only allows assigning tracks within the range of 1 to the total number of
loaded songs.

## Features

### Track Management

- **Track Assigner:** Automatically set tracks based on file names.
- **Conflict Resolution:** The app detects track issues such as tracks being in an invalid range or if there are
  duplicates. If a song has an invalid track, the app will automatically assign it a valid one.
- **Drag & Drop:** Reorder songs by simply dragging them in the list.
- **Editing:** Insert, switch tracks, and modify MP3 fields (title, artist, genre, etc.).
- **Search:** Search loaded files with multi-result capabilities.

### Playback and system integration

- Built-in Player: Listen to your tracks directly within the app.
- System Integration: Open songs in your default music player or reveal them directly in your file explorer.

### UI

Built with JavaFX and the [AtlantaFX](https://github.com/mkpaz/atlantafx) theme library.

- **Cross-Platform:** Native support for Windows, macOS, and Linux.
- **Themes:** Includes 7 built-in themes.
- **Native Menubar:** If enabled, the app can use the native menubar of the operating system (if the platform supports
  it).
- **Localization:** Supports English and Hungarian.

---

### File locations

| Platform    | Logs                                       | Preferences                                                              |
| ----------- | ------------------------------------------ | ------------------------------------------------------------------------ |
| **Windows** | `%APPDATA%\MP3Reorder\logs`                | `%APPDATA%\MP3Reorder\preferences.properties`                            |
| **Linux**   | `~/.MP3Reorder/logs`                       | `~/.MP3Reorder/preferences.properties`                                   |
| **macOS**   | `~/Library/Logs/MP3Reorder`                | `~/Library/Application Support/MP3Reorder/preferences.properties`        |

## Getting Started

### Prerequisites

- JDK 25

### Run the App

```bash
git clone https://github.com/yourusername/MP3Reorder.git
cd MP3Reorder
./gradlew run
```

### Build Executable

To build a native, standalone executable for your current operating system using Jlink and JPackage:

```bash
./gradlew jpackageImage
```

## Screenshots

![Start screen](Readme/Start.png)
![Example](Readme/Example_1.png)
![Themes](Readme/Themes.png)

## Libraries used

- [mp3agic](https://github.com/mpatric/mp3agic)
- [atlantafx](https://github.com/mkpaz/atlantafx)
