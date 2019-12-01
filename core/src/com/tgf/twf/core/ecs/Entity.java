package com.tgf.twf.core.ecs;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * An entity which is essentially an {@link EntityId} and set of associated {@link Component}.
 * {@link Component}s are identified by their class; it is only possible to attach a single component of the same class to an identity.
 * {@link Component}s should be attached and detached via the {@link EntityContainer} so that entity events can be sent as expected.
 */
public class Entity {
    private final EntityId entityId;

    // using a linked hash map so the order of the components attachments is preserved
    // this is useful to fire components detaching events in the reverse order when detaching an entity
    private final LinkedHashMap<Class<? extends Component>, Component> components;

    Entity(final EntityId entityId) {
        this.entityId = entityId;
        components = new LinkedHashMap<>();
    }

    public EntityId getEntityId() {
        return entityId;
    }

    public <T extends Component> T getComponent(final Class<T> clazz) {
        return (T) components.get(clazz);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entity)) {
            return false;
        }
        final Entity entity = (Entity) o;
        return Objects.equals(entityId, entity.entityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId);
    }

    @Override
    public String toString() {
        return entityId.toString();
    }

    // this method is package visible on purpose; use EntityContainer#attachComponent instead
    void attachComponent(final Component component) {
        if (!component.getEntity().equals(this)) {
            throw new IllegalArgumentException("Component is not associated to this entity " + getEntityId());
        }
        if (components.containsKey(component.getClass())) {
            throw new IllegalArgumentException("Entity " + entityId + " already contains component " + component.getClass().getSimpleName());
        }
        components.put(component.getClass(), component);
    }

    // this method is package visible on purpose; use EntityContainer#detachComponent instead
    <T extends Component> T detachComponent(final Class<T> clazz) {
        return (T) components.remove(clazz);
    }

    Collection<Component> getComponents() {
        return components.values();
    }
}
