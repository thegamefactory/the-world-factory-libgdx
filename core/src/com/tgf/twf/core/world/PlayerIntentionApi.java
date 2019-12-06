package com.tgf.twf.core.world;

import com.tgf.twf.core.ecs.Entity;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.world.task.ConstructTask;
import lombok.RequiredArgsConstructor;

/**
 * An world service to invoke to expressed player intentions (captured by input).
 */
@RequiredArgsConstructor
public class PlayerIntentionApi {
    private final World world;

    public boolean build(final BuildingType buildingType, final Position position) {
        if (world.getGeoMap().isPositionOccupied(position.x, position.y)) {
            return false;
        }

        final Building building = new Building(buildingType);
        Entity.builder()
                .withComponent(building)
                .withComponent(position)
                .buildAndAttach();

        world.getTaskSystem().addTask(
                new ConstructTask(building, position)
        );

        return true;
    }
}
