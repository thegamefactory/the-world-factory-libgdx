package com.tgf.twf.core.ecs;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * An entity which is essentially an {@link EntityId} and set of associated {@link Component}.
 * {@link Component}s are identified by their class; it is only possible to attach a single component of the same class to an identity.
 * {@link Component}s should be attached and detached via the {@link Entities} so that entity events can be sent as expected.
 */
public class Entity {
    private final EntityId entityId;

    // using a linked hash map so the order of the components attachments is preserved
    // this is useful to fire components detaching events in the reverse order when detaching an entity
    private final LinkedHashMap<Class<?>, Component<?>> components;

    private Entity(final EntityId entityId) {
        this.entityId = entityId;
        components = new LinkedHashMap<>();
    }

    public EntityId getEntityId() {
        return entityId;
    }

    public <StateT> boolean hasComponent(final Class<StateT> clazz) {
        return components.containsKey(clazz);
    }

    public <StateT> Component<StateT> getComponent(final Class<StateT> clazz) {
        return (Component<StateT>) components.get(clazz);
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

    public <StateT> Component<StateT> attachComponent(final StateT state) {
        final Component<StateT> component = new Component<>(this, state);
        attachComponent(component);
        return component;
    }

    private <StateT> void attachComponent(final Component<StateT> component) {
        if (!component.getEntity().equals(this)) {
            throw new IllegalArgumentException("Component is not associated to this entity " + getEntityId());
        }
        if (components.containsKey(component.getStateClass())) {
            throw new IllegalArgumentException("Entity " + entityId + " already contains component " + component.getState().getClass().getSimpleName());
        }
        components.put(component.getStateClass(), component);
        Entities.getInstance().attachComponent(component);
    }

    public static Builder builder() {
        return new Builder();
    }

    @RequiredArgsConstructor
    public static class Builder {
        private final List<Object> components = new LinkedList<>();

        public <StateT> Builder withComponent(final StateT stateT) {
            this.components.add(stateT);
            return this;
        }

        public Entity buildAndAttach() {
            final Entity entity = new Entity(Entities.getInstance().allocateEntityId());
            components.forEach(
                    state -> entity.attachComponent(new Component<>(entity, state))
            );
            Entities.getInstance().attachEntity(entity);
            return entity;
        }
    }

    Collection<Component<?>> getComponents() {
        return components.values();
    }
}
