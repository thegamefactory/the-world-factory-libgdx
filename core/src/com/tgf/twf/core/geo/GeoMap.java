package com.tgf.twf.core.geo;

import com.tgf.twf.core.ecs.ComponentLifecycleListener;
import com.tgf.twf.core.ecs.Entity;

import java.util.Optional;

/**
 * A class for geographical position lookups.
 * It is kept up to date with the world by listening to the attachment and detachment of entities {@link PositionComponent}s.
 */
public class GeoMap implements ComponentLifecycleListener<PositionComponent> {
    private final Vector2 size;
    private final Entity[] entities;

    public GeoMap(final Vector2 size) {
        this.size = size;
        entities = new Entity[size.x * size.y];
    }

    public Optional<Entity> getEntityAt(final Vector2 position) {
        return Optional.ofNullable(entities[getIndex(position)]);
    }

    @Override
    public void onComponentAttached(final Entity entity, final PositionComponent positionComponent) {
        final int index = getIndex(positionComponent.getPosition());
        if (entities[index] == null) {
            entities[index] = entity;
        } else {
            throw new IllegalStateException(
                    "Entity " + entity + " is at position " + positionComponent.getPosition() + " already occupied by " + entities[index]);
        }
    }

    @Override
    public void onComponentDetached(final Entity entity, final PositionComponent positionComponent) {
        final int index = getIndex(positionComponent.getPosition());
        if (entities[index] != null) {
            entities[index] = null;
        } else {
            throw new IllegalStateException(
                    "Entity " + entity + " is at non-registered position " + positionComponent.getPosition());
        }
    }

    private int getIndex(final Vector2 position) {
        return position.x * size.y + position.y;
    }
}
