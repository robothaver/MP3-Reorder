package com.robothaver.mp3reorder.mp3.controls.table.draganddrop;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.skin.VirtualFlow;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import static com.robothaver.mp3reorder.mp3.controls.table.draganddrop.DragAndDropUtils.NANOSECOND_TO_SECOND;

@Getter
@Setter
@Log4j2
public class TableViewScrollAnimatorImpl implements TableViewScrollAnimator {
    private final ObjectProperty<VirtualFlow<?>> virtualFlowProperty = new SimpleObjectProperty<>();
    private final AnimationTimer animationTimer;
    private double pixelsToMove = 300;
    private double scrollDelta;

    public TableViewScrollAnimatorImpl() {
        animationTimer = getAnimationTimer();
    }

    @Override
    public void setScrollDelta(double delta) {
        scrollDelta = delta;
    }

    @Override
    public void setRunning(boolean running) {
        if (running) animationTimer.start();
        else animationTimer.stop();
    }

    @Override
    public ObjectProperty<VirtualFlow<?>> virtualFlowProperty() {
        return virtualFlowProperty;
    }

    private AnimationTimer getAnimationTimer() {
        return new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long nowNs) {
                if (scrollDelta != 0 && lastUpdate > 0) {
                    double deltaTime = (nowNs - lastUpdate) / NANOSECOND_TO_SECOND;

                    if (virtualFlowProperty.get() != null) {
                        virtualFlowProperty.get().scrollPixels(pixelsToMove * scrollDelta * deltaTime);
                    } else {
                        log.warn("Cannot scroll because VirtualFlow is null");
                    }
                }
                lastUpdate = nowNs;
            }
        };
    }
}
