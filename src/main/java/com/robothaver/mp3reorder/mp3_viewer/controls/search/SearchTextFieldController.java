package com.robothaver.mp3reorder.mp3_viewer.controls.search;

import atlantafx.base.controls.CustomTextField;
import com.robothaver.mp3reorder.core.BaseController;
import com.robothaver.mp3reorder.mp3_viewer.MP3Model;

import java.util.function.Consumer;

public class SearchTextFieldController extends BaseController<CustomTextField> {

    public SearchTextFieldController(MP3Model mp3Model, Consumer<Integer> onSelectedIndexChanged) {
        SearchTextFieldModel songSearch = mp3Model.getSongSearch();
        SearchTextFieldInteractor interactor = new SearchTextFieldInteractor(mp3Model, songSearch, onSelectedIndexChanged);
        viewBuilder = new SearchTextFieldViewBuilder(
                songSearch,
                interactor::selectPrevious,
                interactor::selectNext,
                interactor::onSearchQueryChanged,
                songSearch::clear
        );
    }
}
