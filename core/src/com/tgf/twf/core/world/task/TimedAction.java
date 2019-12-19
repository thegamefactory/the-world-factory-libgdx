package com.tgf.twf.core.world.task;

import com.tgf.twf.core.util.CompletionCallback;
import com.tgf.twf.core.util.Timer;
import com.tgf.twf.core.world.storage.Inventory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;

import java.time.Duration;

/**
 * A generic {@link Action} based on a {@link Timer}
 */
@AllArgsConstructor
public class TimedAction implements Action {
    @NonNull
    private final String name;

    @NonNull
    private final Timer timer;

    @NonNull
    private final Inventory cost;

    @NonNull
    private final Inventory prodction;

    @Builder
    public static TimedAction create(
            final String name,
            final Duration duration,
            final CompletionCallback completionCallback,
            final Inventory cost,
            final Inventory prodction) {
        return new TimedAction(name, new Timer(duration, completionCallback), cost, prodction);
    }

    @Override
    public boolean isComplete() {
        return timer.isComplete();
    }

    @Override
    public void update(final Duration delta) {
        timer.tick(delta);
    }

    @Override
    public Inventory getCost() {
        return cost;
    }

    @Override
    public Inventory getProduction() {
        return prodction;
    }

    @Override
    public String getName() {
        return name;
    }
}
