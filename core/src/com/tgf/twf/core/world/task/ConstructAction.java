package com.tgf.twf.core.world.task;

import com.tgf.twf.core.world.Building;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

/**
 * An {@link Action} implementation which constructs a building (by updating its {@link Building}).
 */
@RequiredArgsConstructor
public class ConstructAction implements Action {
    private final Building buildingComponent;

    @Override
    public boolean isComplete() {
        return buildingComponent.isBuilt();
    }

    @Override
    public void update(final Duration delta) {
        buildingComponent.build(delta);
    }
}
