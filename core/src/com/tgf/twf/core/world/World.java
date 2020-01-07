package com.tgf.twf.core.world;

import com.tgf.twf.core.ecs.Entity;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.geo.GeoMap;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.agents.Agent;
import com.tgf.twf.core.world.agents.AgentSystem;
import com.tgf.twf.core.world.agriculture.AgricultureSystem;
import com.tgf.twf.core.world.building.Building;
import com.tgf.twf.core.world.building.BuildingType;
import com.tgf.twf.core.world.daytimesystem.Daytime;
import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.core.world.storage.AnyResourceTypeFixedCapacity;
import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.storage.Storage;
import com.tgf.twf.core.world.terrain.BerryTerrainGenerator;
import com.tgf.twf.core.world.terrain.CoastTerrainGenerator;
import com.tgf.twf.core.world.terrain.ForrestTerrainGenerator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Random;

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
    private final AgentSystem agentSystem;
    private final AgricultureSystem agricultureSystem;

    public World(final Vector2 size) {
        this.size = size;

        geoMap = new GeoMap(size);
        new CoastTerrainGenerator().generate(geoMap);
        new BerryTerrainGenerator(new Random(), Rules.BERRY_RATIO).generate(geoMap);
        new ForrestTerrainGenerator(new Random(), Rules.FORREST_RATIO).generate(geoMap);

        agentSystem = new AgentSystem(geoMap);
        agricultureSystem = new AgricultureSystem(agentSystem);

        final Vector2 initialPosition = new Vector2(size.x / 2, size.y / 2);
        final Building farm = Building.createEntity(BuildingType.FARM, initialPosition);
        farm.setConstructed();
        farm.getRelatedComponent(Storage.class).forceStore(ResourceType.FOOD, Rules.INITIAL_FOOD_STORAGE);

        for (int i = 0; i < Rules.INITIAL_AGENT_COUNT; i++) {
            Entity.builder()
                    .withComponent(new Agent(farm, initialPosition))
                    .withComponent(new Storage(new AnyResourceTypeFixedCapacity(Rules.AGENT_STORAGE_CAPACITY)))
                    .buildAndAttach();
        }
    }

    @Override
    public void tick() {
        agricultureSystem.tick();
        agentSystem.tick();
        Daytime.INSTANCE.tick();
    }
}
