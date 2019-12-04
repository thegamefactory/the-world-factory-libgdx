package com.tgf.twf.core.geo;

import com.google.common.collect.ImmutableList;
import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.ComponentLifecycleListener;
import com.tgf.twf.core.ecs.ComponentStateUpdateListener;
import com.tgf.twf.core.ecs.Entity;
import com.tgf.twf.core.world.Building;
import com.tgf.twf.core.world.task.Agent;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for geographical position lookups.
 * It is kept up to date with the world by listening to the attachment and detachment of entities {@link Position}s.
 */
public class GeoMap implements ComponentLifecycleListener<Position>, ComponentStateUpdateListener<Position> {
    private final Vector2 size;
    private final List<Entity>[] entities;

    public GeoMap(final Vector2 size) {
        this.size = size;
        entities = new List[size.x * size.y];
    }

    public Optional<Component<Building>> getBuildingAt(final int x, final int y) {
        return getEntityAt(x, y).stream()
                .map(entity -> entity.getComponent(Building.class))
                .filter(Objects::nonNull)
                .findFirst();
    }

    public List<Component<Agent>> getAgentsAt(final int x, final int y) {
        return getEntityAt(x, y).stream()
                .map(entity -> entity.getComponent(Agent.class))
                .filter(Objects::nonNull)
                .collect(ImmutableList.toImmutableList());
    }

    public boolean isPositionOccupied(final int x, final int y) {
        return getEntityAt(x, y).stream()
                .anyMatch(e -> e.hasComponent(Building.class));
    }

    @Override
    public void onComponentAttached(final Component<Position> positionComponent) {
        final Position position = positionComponent.getState();
        final int index = getIndex(position);
        if (entities[index] == null) {
            entities[index] = new LinkedList<>();
        }
        entities[index].add(positionComponent.getEntity());
    }

    @Override
    public void onComponentStateUpdated(final Component<Position> component, final Position oldState) {
        entities[getIndex(oldState)].remove(component.getEntity());
        onComponentAttached(component);
    }

    private int getIndex(final Position position) {
        return position.x * size.y + position.y;
    }

    private List<Entity> getEntityAt(final int x, final int y) {
        return Optional.ofNullable(entities[x * size.y + y]).orElse(ImmutableList.of());
    }
}
