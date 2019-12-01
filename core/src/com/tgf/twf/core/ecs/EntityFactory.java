package com.tgf.twf.core.ecs;

/**
 * An entity factory which enforces that created {@link Entity} objects have monotonically increasing {@link EntityId}s.
 */
public class EntityFactory {
    private int nextId = 0;

    public Entity create() {
        return new Entity(new EntityId(nextId++));
    }
}
