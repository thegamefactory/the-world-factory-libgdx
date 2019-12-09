package com.tgf.twf.core.world.task;

import com.tgf.twf.core.util.Timer;
import com.tgf.twf.core.world.storage.Storage;
import lombok.AllArgsConstructor;

import java.time.Duration;

/**
 * A generic {@link Action} based on a {@link Timer}
 */
@AllArgsConstructor
public class TimedAction implements Action {
    private final Timer timer;
    private final Storage.Inventory cost;

    @Override
    public boolean isComplete() {
        return timer.isComplete();
    }

    @Override
    public void update(final Duration delta) {
        timer.tick(delta);
    }

    @Override
    public Storage.Inventory getCost() {
        return cost;
    }
}
