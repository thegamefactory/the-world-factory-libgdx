package com.tgf.twf.core.world.agriculture;

import com.tgf.twf.core.util.Timer;

import java.time.Duration;

public class GrowingState implements Field.State {
    private final Timer timer;

    public GrowingState(final Duration duration) {
        this.timer = new Timer(duration);
    }

    @Override
    public Class<? extends Field.State> tick(final Duration delta) {
        timer.tick(delta);
        return timer.isComplete() ? GrownState.class : this.getClass();
    }

    @Override
    public void onStateEnter() {

    }
}