package com.tgf.twf.core.world;

import com.tgf.twf.core.ecs.Entity;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.geo.GeoMap;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.geo.Vector2;
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

    public World(final Vector2 size, final Random random) {
        this.size = size;
        this.random = random;

        geoMap = new GeoMap(size);
        taskSystem = new TaskSystem();

        final Building farmBuilding = new Building(BuildingType.FARM);
        farmBuilding.setConstructed();
        final Entity farm = Entity.builder()
                .withComponent(farmBuilding)
                .withComponent(Position.from(1, 1))
                .buildAndAttach();

        for (int i = 0; i < 3; i++) {
            Entity.builder()
                    .withComponent(new Agent(farm.getComponent(Building.class)))
                    .withComponent(Position.from(1, 1))
                    .buildAndAttach();
        }
    }

    @Override
    public void update(final Duration delta) {
        taskSystem.update(delta);
    }
}
