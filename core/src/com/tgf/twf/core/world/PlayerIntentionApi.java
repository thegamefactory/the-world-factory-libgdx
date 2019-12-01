package com.tgf.twf.core.world;

import com.tgf.twf.core.ecs.Entity;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.world.task.BuildTask;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerIntentionApi {
    private final World world;

    public boolean build(final BuildingType buildingType, final Position position) {
        if (world.getGeoMap().isPositionOccupied(position.x, position.y)) {
            return false;
        }

        final BuildingState buildingState = new BuildingState(buildingType);

        Entity.builder()
                .withComponent(buildingState)
                .withComponent(position)
                .buildAndAttach();

        world.getTaskSystem().addTask(
                new BuildTask(buildingState, position)
        );

        return true;
    }
}
