package com.tgf.twf.core.util;

import java.time.Duration;

/**
 * A timer that waits the given {@link Duration}, and completes and invokes the {@link CompletionCallback} once the action has been
 * ticked for a total time corresponding to the total duration.
 */
public class Timer {
    private Duration remainingDuration;
    private final CompletionCallback completionCallback;

    public Timer(final Duration remainingDuration, final CompletionCallback completionCallback) {
        this.remainingDuration = remainingDuration;
        this.completionCallback = completionCallback == null ? CompletionCallback.IDENTITY : completionCallback;
    }

    public Timer(final Duration remainingDuration) {
        this.remainingDuration = remainingDuration;
        completionCallback = CompletionCallback.IDENTITY;
    }

    @FunctionalInterface
    public interface CompletionCallback {
        CompletionCallback IDENTITY = () -> {
        };

        void complete();
    }

    public boolean isComplete() {
        return remainingDuration.isZero() || remainingDuration.isNegative();
    }

    public void tick(final Duration delta) {
        remainingDuration = remainingDuration.minus(delta);

        if (isComplete()) {
            remainingDuration = Duration.ZERO;
            completionCallback.complete();
        }
    }

    @Override
    public String toString() {
        return "[remainingDuration=" + remainingDuration + "]";
    }
}
