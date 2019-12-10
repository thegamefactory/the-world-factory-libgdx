package com.tgf.twf.core.world.agriculture;

import com.tgf.twf.core.util.Timer;

import java.time.Duration;

/**
 * The {@link Field.State}  of that field when it's growing.
 * This {@link Field.State}  essentially waits for a certain amount of time to elapse and then transitions to {@link GrownState}.
 */
public class GrowingState implements Field.State {
    private final Timer timer;

    public GrowingState(final Duration duration) {
        this.timer = new Timer(duration);
    }

    @Override
    public Class<? extends Field.State> tick(final Duration delta) {
        timer.tick(delta);
        return timer.isComplete() ? GrownState.class : null;
    }

    @Override
    public void onStateEnter() {

    }

    @Override
    public String toString() {
        return "GrowingState[timer=" + timer + "]";
    }
}