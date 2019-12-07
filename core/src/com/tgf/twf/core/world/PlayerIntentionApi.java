package com.tgf.twf.core.world;

import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.task.ConstructTask;
import com.tgf.twf.input.ExecutionMode;
import lombok.RequiredArgsConstructor;

/**
 * An world service to invoke to expressed player intentions (captured by input).
 */
@RequiredArgsConstructor
public class PlayerIntentionApi {
    private final World world;

    public boolean build(final BuildingType buildingType, final Vector2 position, final ExecutionMode executionMode) {
        if (world.getGeoMap().isPositionOccupied(position.x, position.y)) {
            return false;
        }
        if (ExecutionMode.DRY_RUN.equals(executionMode)) {
            return true;
        }

        final Position pos = Position.from(position);
        final Building building = Building.createEntity(buildingType, pos);
        world.getTaskSystem().addTask(
                new ConstructTask(building, position)
        );

        return true;
    }
}
