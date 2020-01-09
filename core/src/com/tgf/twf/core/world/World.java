package com.tgf.twf.core.world;

import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.geo.GeoMap;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.agents.AgentSystem;
import com.tgf.twf.core.world.agriculture.AgricultureSystem;
import com.tgf.twf.core.world.building.Building;
import com.tgf.twf.core.world.building.BuildingType;
import com.tgf.twf.core.world.daytimesystem.DaytimeSystem;
import com.tgf.twf.core.world.home.Home;
import com.tgf.twf.core.world.home.HomeSystem;
import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.storage.Storage;
import com.tgf.twf.core.world.terrain.BerryTerrainGenerator;
import com.tgf.twf.core.world.terrain.CoastTerrainGenerator;
import com.tgf.twf.core.world.terrain.ForrestTerrainGenerator;
import lombok.Getter;

import java.util.Random;

/**
 * Top class representing the world.
 */
public class World implements System {
    @Getter
    private final Vector2 size;

    @Getter
    private final GeoMap geoMap;

    @Getter
    private final AgentSystem agentSystem;

    private final AgricultureSystem agricultureSystem;

    @Getter
    private final DaytimeSystem daytimeSystem;

    private final HomeSystem homeSystem;

    public World(final Vector2 size, final DaytimeSystem daytimeSystem) {
        this.size = size;

        geoMap = new GeoMap(size);
        new CoastTerrainGenerator().generate(geoMap);
        new BerryTerrainGenerator(new Random(), Rules.BERRY_RATIO).generate(geoMap);
        new ForrestTerrainGenerator(new Random(), Rules.FORREST_RATIO).generate(geoMap);

        this.daytimeSystem = daytimeSystem;
        agentSystem = new AgentSystem(daytimeSystem, geoMap);
        agricultureSystem = new AgricultureSystem(agentSystem);
        homeSystem = new HomeSystem();

        final Vector2 initialPosition = new Vector2(size.x / 2, size.y / 2);
        final Building farmhouse = Building.createEntity(BuildingType.FARMHOUSE, initialPosition);

        farmhouse.setConstructed();
        farmhouse.getRelatedComponent(Storage.class).forceStore(ResourceType.FOOD, Rules.INITIAL_FOOD_STORAGE);

        final Home home = farmhouse.getRelatedComponent(Home.class);
        for (int i = 0; i < Rules.INITIAL_AGENT_COUNT; i++) {
            homeSystem.createAgent(home);
        }
    }

    @Override
    public void tick() {
        daytimeSystem.tick();
        agricultureSystem.tick();
        agentSystem.tick();
        homeSystem.tick();
    }
}
