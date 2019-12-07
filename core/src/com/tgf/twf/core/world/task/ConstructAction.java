package com.tgf.twf.core.world.task;

import com.tgf.twf.core.world.Building;
import com.tgf.twf.core.world.Storage;
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
    public Storage.Inventory getCost() {
        return Cost.ONE_FOOD;
    }
}
