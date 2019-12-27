package com.tgf.twf.core.geo;

import com.google.common.collect.ImmutableList;
import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.Entities;
import com.tgf.twf.core.world.agents.Agent;
import com.tgf.twf.core.world.building.Building;
import com.tgf.twf.core.world.terrain.TerrainMap;
import com.tgf.twf.core.world.terrain.TerrainType;
import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A class for geographical position lookups.
 * It is kept up to date with the world by listening to the attachment and detachment of entities {@link Position}s.
 */
public class GeoMap implements TerrainMap {
    @Getter
    private final Vector2 size;
    private final List<Agent>[] agents;
    private final Building[] buildings;
    private final TerrainType[] terrain;

    public GeoMap(final Vector2 size) {
        this.size = size;
        agents = new List[size.x * size.y];
        buildings = new Building[size.x * size.y];
        terrain = new TerrainType[size.x * size.y];
        Arrays.fill(terrain, TerrainType.GRASS);

        Entities.registerComponentEventListener(this::handle, Building.class, Component.CreationEvent.class);
        Entities.registerComponentEventListener(this::handle, Agent.class, Component.CreationEvent.class);
        Entities.registerComponentEventListener(this::handle, Agent.class, Agent.MoveEvent.class);
    }

    public Building getBuildingAt(final Vector2 position) {
        return getBuildingAt(position.x, position.y);
    }

    public Building getBuildingAt(final int x, final int y) {
        return buildings[getIndex(x, y)];
    }

    public List<Agent> getAgentsAt(final int x, final int y) {
        final List<Agent> agentsAt = agents[x * size.y + y];
        return agentsAt == null ? ImmutableList.of() : agentsAt;
    }

    @Override
    public TerrainType getTerrainAt(final Vector2 position) {
        return terrain[getIndex(position)];
    }

    @Override
    public void setTerrainAt(final Vector2 position, final TerrainType terrainType) {
        terrain[getIndex(position)] = terrainType;
    }

    public boolean isInBounds(final Vector2 position) {
        return isInBounds(position.x, position.y);
    }

    public boolean isInBounds(final int x, final int y) {
        return x >= 0 && x < size.x && y > 0 && y < size.y;
    }

    public boolean isBuildingAt(final Vector2 position) {
        return isBuildingAt(position.x, position.y);
    }

    public boolean isBuildingAt(final int x, final int y) {
        return getBuildingAt(x, y) != null;
    }

    public void handle(final Building sender, final Component.CreationEvent event) {
        final int index = getIndex(sender.getPosition().x, sender.getPosition().y);
        buildings[index] = sender;
    }

    public void handle(final Agent sender, final Component.CreationEvent event) {
        placeAgent(sender, sender.getPosition());
    }

    public void handle(final Agent sender, final Agent.MoveEvent event) {
        agents[getIndex(sender.getPosition().x, sender.getPosition().y)].remove(sender);
        placeAgent(sender, event.getNewPosition());
    }

    private void placeAgent(final Agent sender, final Vector2 position) {
        final int index = getIndex(position.x, position.y);
        if (agents[index] == null) {
            agents[index] = new LinkedList<>();
        }
        agents[index].add(sender);
    }

    private int getIndex(final Vector2 position) {
        return position.x * size.y + position.y;
    }

    private int getIndex(final int x, final int y) {
        return x * size.y + y;
    }
}
