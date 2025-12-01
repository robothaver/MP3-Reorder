package com.robothaver.mp3reorder.core;

import javafx.util.Builder;

public abstract class BaseController<S> {
    protected Builder<S> viewBuilder;

    public S getView() {
        return viewBuilder.build();
    }
}

