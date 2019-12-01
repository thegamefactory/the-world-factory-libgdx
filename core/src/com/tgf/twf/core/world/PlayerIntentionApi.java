package com.tgf.twf.core.world;

import com.tgf.twf.core.ecs.Entity;
import com.tgf.twf.core.geo.PositionComponent;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.task.BuildTask;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerIntentionApi {
    private final World world;

    public boolean build(final Vector2 position, final BuildingType buildingType) {
        if (world.getGeoMap().getEntityAt(position).isPresent()) {
            return false;
        }

        final Entity entity = world.createEntity();
        final BuildingComponent buildingComponent = new BuildingComponent(entity, buildingType);
        final PositionComponent positionComponent = new PositionComponent(entity, position);
        world.attachComponents(
                buildingComponent,
                positionComponent
        );
        world.getTaskSystem().addTask(
                new BuildTask(buildingComponent, positionComponent)
        );

        return true;
    }
}
