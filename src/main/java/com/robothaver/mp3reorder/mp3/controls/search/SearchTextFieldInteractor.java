package com.robothaver.mp3reorder.mp3.controls.search;

import com.robothaver.mp3reorder.mp3.MP3Model;
import com.robothaver.mp3reorder.mp3.domain.Song;
import javafx.collections.ObservableList;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class SearchTextFieldInteractor {
    private final MP3Model mp3Model;
    private final SearchTextFieldModel model;
    private final Consumer<Integer> onSelectedIndexChanged;

    public void selectPrevious() {
        if (model.getResults().size() < 2) return;
        int selectedIndex = model.getSelectedResultIndex().get();
        if (selectedIndex == 0) {
            int lastIndex = model.getResults().size() - 1;
            model.getSelectedResultIndex().set(lastIndex);
            onSelectedIndexChanged.accept(mp3Model.getSongs().indexOf(model.getResults().get(lastIndex)));
        } else {
            model.getSelectedResultIndex().set(selectedIndex - 1);
            onSelectedIndexChanged.accept(mp3Model.getSongs().indexOf(model.getResults().get(selectedIndex - 1)));
        }
    }

    public void selectNext() {
        if (model.getResults().size() < 2) return;
        int selectedIndex = model.getSelectedResultIndex().get();
        int lastIndex = model.getResults().size() - 1;
        if (selectedIndex == lastIndex) {
            model.getSelectedResultIndex().set(0);
            onSelectedIndexChanged.accept(mp3Model.getSongs().indexOf(model.getResults().getFirst()));
        } else {
            model.getSelectedResultIndex().set(selectedIndex + 1);
            onSelectedIndexChanged.accept(mp3Model.getSongs().indexOf(model.getResults().get(selectedIndex + 1)));
        }
    }

    public void onSearchQueryChanged() {
        String searchQuery = model.getSearchQuery().get().trim();
        ObservableList<Song> results = model.getResults();
        results.clear();
        if (searchQuery.isBlank()) return;

        for (Song song : mp3Model.getSongs()) {
            if (song.getFileName().equalsIgnoreCase(searchQuery) || song.getFileName().toLowerCase().contains(searchQuery.toLowerCase())) {
                results.add(song);
            }
        }
        if (!results.isEmpty()) {
            int firstIndex = mp3Model.getSongs().indexOf(results.getFirst());
            model.getSelectedResultIndex().set(0);
            mp3Model.selectedSongIndexProperty().set(firstIndex);
        } else {
            model.getSelectedResultIndex().set(-1);
        }
    }
}
