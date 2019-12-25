package com.tgf.twf.core.world.agents;

import com.tgf.twf.core.geo.Vector2;

/**
 * An {@link Action} which invokes a {@link #onCycleComplete(Agent)} callback each time the tick counter reaches a certain value, and then resets
 * the tick counter, until the callback returns true.
 */
public abstract class CyclicAction implements Action {
    private final Vector2 position;
    private final int tickCycle;
    private int tickCounter = 0;

    public CyclicAction(final Vector2 position, final int tickCycle) {
        this.position = position;
        this.tickCycle = tickCycle;
    }

    @Override
    public Vector2 getPosition() {
        return this.position;
    }

    @Override
    public boolean tick(final Agent agent) {
        tickCounter++;
        if (tickCycle == tickCounter) {
            tickCounter = 0;
            return onCycleComplete(agent);
        } else {
            return false;
        }
    }

    public abstract boolean onCycleComplete(final Agent agent);
}
