package com.tgf.twf.core.world.task;

import com.tgf.twf.core.world.storage.Storage;
import lombok.AllArgsConstructor;

import java.time.Duration;

/**
 * A generic {@link Action} that waits the given {@link Duration}, and completes and invokes the {@link CompletionCallback} once the action has been
 * updated for a total time corresponding to the total duration.
 */
@AllArgsConstructor
public class TimedAction implements Action {
    private Duration remainingDuration;
    private final CompletionCallback completionCallback;
    private final Storage.Inventory cost;

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

    @Override
    public Storage.Inventory getCost() {
        return cost;
    }
}
