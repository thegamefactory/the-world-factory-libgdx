package com.tgf.twf.core.world.agriculture;

/**
 * The {@link Field.State}  of that field when it's growing.
 * This {@link Field.State}  essentially waits for a certain amount of time to elapse and then transitions to {@link GrownState}.
 */
public class GrowingState implements Field.State {
    private int remainingTicks;

    public GrowingState(final int remainingTicks) {
        this.remainingTicks = remainingTicks;
    }

    @Override
    public Class<? extends Field.State> tick() {
        remainingTicks--;
        return remainingTicks <= 0 ? GrownState.class : null;
    }

    @Override
    public void onStateEnter() {

    }

    @Override
    public String toString() {
        return "GrowingState[remainingTicks=" + remainingTicks + "]";
    }
}