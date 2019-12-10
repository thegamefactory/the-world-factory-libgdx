package com.tgf.twf.core.world;

import com.tgf.twf.core.ecs.Entity;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.geo.GeoMap;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.agriculture.AgricultureSystem;
import com.tgf.twf.core.world.building.Building;
import com.tgf.twf.core.world.building.BuildingType;
import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.storage.Storage;
import com.tgf.twf.core.world.task.Agent;
import com.tgf.twf.core.world.task.TaskSystem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

/**
 * Top class representing the world.
 */
@RequiredArgsConstructor
public class World implements System {
    @Getter
    private final Vector2 size;

    @Getter
    private final GeoMap geoMap;

    @Getter
    private final TaskSystem taskSystem;
    private final AgricultureSystem agricultureSystem;

    public World(final Vector2 size) {
        this.size = size;

        geoMap = new GeoMap(size);
        taskSystem = new TaskSystem();
        agricultureSystem = new AgricultureSystem(taskSystem);

        final Position initialPosition = new Position(size.x / 2, size.y / 2);
        final Building farm = Building.createEntity(BuildingType.FARM, initialPosition);
        farm.setConstructed();
        farm.getRelatedComponent(Storage.class).forceStore(ResourceType.FOOD, Rules.INITIAL_FOOD_STORAGE);

        for (int i = 0; i < Rules.INITIAL_AGENT_COUNT; i++) {
            Entity.builder()
                    .withComponent(new Agent(farm))
                    .withComponent(new Position(initialPosition))
                    .buildAndAttach();
        }
    }

    @Override
    public void update(final Duration delta) {
        agricultureSystem.update(delta);
        taskSystem.update(delta);
    }
}
