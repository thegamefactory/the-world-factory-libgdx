package com.tgf.twf.core.world.task;

import com.tgf.twf.core.world.storage.Inventory;

import java.time.Duration;

/**
 * {@link Agent} components enqueue {@link Action}s and execute them in sequence.
 */
public interface Action {
    boolean isComplete();

    void update(final Duration delta);

    Inventory getCost();

    String getName();
}
