package com.tgf.twf.core.world.task;

import java.time.Duration;

public class TimedAction implements Action {
    private Duration remainingDuration;
    private final CompletionCallback completionCallback;

    public TimedAction(final Duration totalTime, final CompletionCallback completionCallback) {
        this.remainingDuration = totalTime;
        this.completionCallback = completionCallback;
    }

    @FunctionalInterface
    public interface CompletionCallback {
        void complete();
    }

    @Override
    public boolean isComplete() {
        return remainingDuration.isZero() || remainingDuration.isNegative();
    }

    @Override
    public void update(final Duration delta) {
        remainingDuration = remainingDuration.minus(delta);

        if (isComplete()) {
            remainingDuration = Duration.ZERO;
            completionCallback.complete();
        }
    }
}
