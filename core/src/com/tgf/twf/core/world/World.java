package com.tgf.twf.core.world;

import com.tgf.twf.core.ecs.Entities;
import com.tgf.twf.core.ecs.Entity;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.geo.GeoMap;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.geo.Vector2;
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
        Entities.registerComponentLifecycleListener(geoMap, Position.class);
        Entities.registerComponentStateUpdateListener(geoMap, Position.class);

        taskSystem = new TaskSystem();
        Entities.registerComponentLifecycleListener(taskSystem, AgentState.class);

        final Entity farm = Entity.builder()
                .withComponent(new BuildingState(BuildingType.FARM))
                .withComponent(Position.of(1, 1))
                .buildAndAttach();

        for (int i = 0; i < 3; i++) {
            Entity.builder()
                    .withComponent(new AgentState(farm.getComponent(BuildingState.class)))
                    .withComponent(Position.of(1, 1))
                    .buildAndAttach();
        }
    }

    @Override
    public void update(final Duration delta) {
        taskSystem.update(delta);
    }
}
