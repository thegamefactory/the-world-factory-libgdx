package com.tgf.twf.core.world.task;

import com.tgf.twf.core.world.ResourceType;
import javafx.util.Pair;

import java.time.Duration;

/**
 * {@link Agent} components enqueue {@link Action}s and execute them in sequence.
 */
public interface Action {
    boolean isComplete();

    void update(final Duration delta);

    Pair<ResourceType, Integer> getCost();

    class Cost {
        public static final Pair<ResourceType, Integer> FREE = new Pair<>(ResourceType.FOOD, 0);
        public static final Pair<ResourceType, Integer> ONE_FOOD = new Pair<>(ResourceType.FOOD, 1);
    }
}
