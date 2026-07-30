package com.robothaver.mp3reorder.mp3.controls.search;

import atlantafx.base.controls.CustomTextField;
import com.robothaver.mp3reorder.core.BaseController;
import com.robothaver.mp3reorder.mp3.MP3Model;

public class SearchTextFieldController extends BaseController<CustomTextField> {

    public SearchTextFieldController(MP3Model mp3Model) {
        SearchTextFieldModel songSearch = mp3Model.getSongSearch();
        SearchTextFieldInteractor interactor = new SearchTextFieldInteractor(mp3Model, songSearch);
        viewBuilder = new SearchTextFieldViewBuilder(
                songSearch,
                interactor::selectPrevious,
                interactor::selectNext,
                interactor::onSearchQueryChanged,
                songSearch::clear
        );
    }
}
