package com.tgf.twf.core.geo;

import com.google.common.collect.ImmutableList;
import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.ComponentLifecycleListener;
import com.tgf.twf.core.ecs.Entity;
import com.tgf.twf.core.world.AgentState;
import com.tgf.twf.core.world.BuildingState;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for geographical position lookups.
 * It is kept up to date with the world by listening to the attachment and detachment of entities {@link Position}s.
 */
public class GeoMap implements ComponentLifecycleListener<Position> {
    private final Vector2 size;
    private final List<Entity>[] entities;

    public GeoMap(final Vector2 size) {
        this.size = size;
        entities = new List[size.x * size.y];
    }

    public Optional<Component<BuildingState>> getBuildingAt(final int x, final int y) {
        return getEntityAt(x, y).stream()
                .map(entity -> entity.getComponent(BuildingState.class))
                .filter(Objects::nonNull)
                .findFirst();
    }

    public List<Component<AgentState>> getAgentsAt(final int x, final int y) {
        return getEntityAt(x, y).stream()
                .map(entity -> entity.getComponent(AgentState.class))
                .filter(Objects::nonNull)
                .collect(ImmutableList.toImmutableList());
    }

    public boolean isPositionOccupied(final int x, final int y) {
        return getEntityAt(x, y).stream()
                .anyMatch(e -> e.hasComponent(BuildingState.class));
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

    private int getIndex(final Position position) {
        return position.x * size.y + position.y;
    }

    private List<Entity> getEntityAt(final int x, final int y) {
        return Optional.ofNullable(entities[x * size.y + y]).orElse(ImmutableList.of());
    }
}
