package com.tgf.twf.core.world.task;

import com.tgf.twf.core.world.BuildingComponent;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
public class BuildAction implements Action {
    private final BuildingComponent buildingComponent;

    @Override
    public boolean isComplete() {
        return buildingComponent.isBuilt();
    }

    @Override
    public void update(final Duration delta) {
        buildingComponent.build(delta);
    }
}
