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
 */
public class Entity {
    private final EntityId entityId;

    // using a linked hash map so the order of the components attachments is preserved
    // this is useful to fire components detaching events in the reverse order when detaching an entity
    private final LinkedHashMap<Class<?>, Component> components;

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

    public <ComponentT> ComponentT getComponent(final Class<ComponentT> clazz) {
        return (ComponentT) components.get(clazz);
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
        return "entity(id=" + entityId.toString() + ", components=" + components.toString() + ")";
    }

    public void attachComponent(final Component component) {
        if (component.getEntity() != null) {
            throw new IllegalArgumentException("Cannot attach to " + toString() + " because component " + component.getClass() +
                    " is already attached to " + component.getEntity());
        }
        if (components.containsKey(component.getClass())) {
            throw new IllegalArgumentException("Entity " + entityId + " already contains component " + component.getClass().getSimpleName());
        }
        component.setEntity(this);
        components.put(component.getClass(), component);
        Entities.getInstance().attachComponent(component);
    }

    public static Builder builder() {
        return new Builder();
    }

    @RequiredArgsConstructor
    public static class Builder {
        private final List<Component> components = new LinkedList<>();

        public <ComponentT extends Component> Builder withComponent(final ComponentT component) {
            this.components.add(component);
            return this;
        }

        public Entity buildAndAttach() {
            final Entity entity = new Entity(Entities.getInstance().allocateEntityId());
            components.forEach(entity::attachComponent);
            Entities.getInstance().attachEntity(entity);
            return entity;
        }
    }

    Collection<Component> getComponents() {
        return components.values();
    }
}
