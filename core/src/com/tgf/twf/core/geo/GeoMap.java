package com.tgf.twf.core.geo;

import com.google.common.collect.ImmutableList;
import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.Entities;
import com.tgf.twf.core.ecs.Entity;
import com.tgf.twf.core.world.building.Building;
import com.tgf.twf.core.world.task.Agent;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * A class for geographical position lookups.
 * It is kept up to date with the world by listening to the attachment and detachment of entities {@link Position}s.
 */
public class GeoMap {
    private final Vector2 size;
    private final List<Entity>[] entities;

    public GeoMap(final Vector2 size) {
        this.size = size;
        entities = new List[size.x * size.y];

        Entities.registerComponentEventListener(this::handle, Position.class, Component.CreationEvent.class);
        Entities.registerComponentEventListener(this::handle, Position.class, Position.MoveEvent.class);
    }

    public Optional<Building> getBuildingAt(final int x, final int y) {
        final List<Entity> entities = getEntityAt(x, y);

        for (final Entity entity : entities) {
            final Building building = entity.getComponent(Building.class);
            if (null != building) {
                return Optional.of(building);
            }
        }
        return Optional.empty();
    }

    public void getAgentsAt(final int x, final int y, final Agent[] agents) {
        final List<Entity> entities = getEntityAt(x, y);

        int i = 0;
        for (final Entity entity : entities) {
            final Agent agent = entity.getComponent(Agent.class);
            if (null != agent) {
                agents[i++] = agent;
                if (i == agents.length) {
                    break;
                }
            }
        }
        if (i != agents.length) {
            agents[i] = null;
        }
    }

    public boolean isPositionOccupied(final int x, final int y) {
        if (x < 0 || x >= size.x || y < 0 || y >= size.y) {
            return true;
        }

        return getEntityAt(x, y).stream()
                .anyMatch(e -> e.hasComponent(Building.class));
    }

    public List<Entity> getEntityAt(final int x, final int y) {
        return Optional.ofNullable(entities[x * size.y + y]).orElse(ImmutableList.of());
    }

    public void handle(final Position sender, final Component.CreationEvent event) {
        placeEntity(sender.getEntity(), sender.x, sender.y);
    }

    public void handle(final Position sender, final Position.MoveEvent event) {
        entities[getIndex(sender.x, sender.y)].remove(sender.getEntity());
        placeEntity(sender.getEntity(), event.getNewPosition().x, event.getNewPosition().y);
    }

    private void placeEntity(final Entity entity, final int x, final int y) {
        final int index = getIndex(x, y);
        if (entities[index] == null) {
            entities[index] = new LinkedList<>();
        }
        entities[index].add(entity);
    }

    private int getIndex(final int x, final int y) {
        return x * size.y + y;
    }
}
