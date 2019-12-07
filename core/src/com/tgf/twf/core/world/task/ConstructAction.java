package com.tgf.twf.core.world.task;

import com.tgf.twf.core.world.Building;
import com.tgf.twf.core.world.ResourceType;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

/**
 * An {@link Action} implementation which constructs a {@link Building}.
 */
@RequiredArgsConstructor
public class ConstructAction implements Action {
    private final Building building;

    @Override
    public boolean isComplete() {
        return building.isConstructed();
    }

    @Override
    public void update(final Duration delta) {
        building.build(delta);
    }

    @Override
    public Pair<ResourceType, Integer> getCost() {
        return Cost.ONE_FOOD;
    }
}
