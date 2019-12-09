package com.tgf.twf.core.world;

import com.tgf.twf.core.ecs.Entity;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.geo.GeoMap;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.agriculture.AgricultureSystem;
import com.tgf.twf.core.world.building.Building;
import com.tgf.twf.core.world.building.BuildingType;
import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.storage.Storage;
import com.tgf.twf.core.world.task.Agent;
import com.tgf.twf.core.world.task.TaskSystem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Random;

/**
 * Top class representing the world.
 */
@RequiredArgsConstructor
public class World implements System {
    @Getter
    private final Vector2 size;
    private final Random random;

    @Getter
    private final GeoMap geoMap;

    @Getter
    private final TaskSystem taskSystem;
    private final AgricultureSystem agricultureSystem;

    public World(final Vector2 size, final Random random) {
        this.size = size;
        this.random = random;

        geoMap = new GeoMap(size);
        taskSystem = new TaskSystem();
        agricultureSystem = new AgricultureSystem(taskSystem);

        final Building farm = Building.createEntity(BuildingType.FARM, new Position(1, 1));
        farm.setConstructed();
        farm.getRelatedComponent(Storage.class).store(ResourceType.FOOD, 30);

        for (int i = 0; i < 3; i++) {
            Entity.builder()
                    .withComponent(new Agent(farm))
                    .withComponent(Position.from(1, 1))
                    .buildAndAttach();
        }
    }

    @Override
    public void update(final Duration delta) {
        agricultureSystem.update(delta);
        taskSystem.update(delta);
    }
}
