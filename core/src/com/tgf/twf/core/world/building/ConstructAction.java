package com.tgf.twf.core.world.building;

import com.tgf.twf.core.world.storage.Storage;
import com.tgf.twf.core.world.task.Action;
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

    @Override
    public String getName() {
        return "construct";
    }
}
