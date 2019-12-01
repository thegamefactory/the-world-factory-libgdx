package com.tgf.twf.core.ecs;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Container of {@link Entity}.
 * Manages Entities lifecycle, including attaching/detaching them from the container, attaching/detaching {@link Component}s from the entities, and
 * firing corresponding events.
 * {@link EntityLifecycleListener} and {@link ComponentLifecycleListener}s can subscribe/unsubscribe to events fired by the container.
 */
public class EntityContainer implements EntityEventsProducer {
    private final Map<EntityId, Entity> entities = new HashMap<>();
    private final List<EntityLifecycleListener> entityLifecycleListeners = new LinkedList<>();
    private final Multimap<Class<? extends Component>, ComponentLifecycleListener> componentLifecycleListeners = LinkedHashMultimap.create();

    /**
     * Attaches an entity
     * If the entity contains components, all corresponding components attachment events will be fired.
     * The entity object passed as parameter is not affected by this operation.
     */
    public void attachEntity(final Entity entity) {
        if (entities.containsKey(entity.getEntityId())) {
            throw new IllegalArgumentException("Duplicate entity id: " + entity.getEntityId());
        }
        entities.put(entity.getEntityId(), entity);

        entityLifecycleListeners.forEach(l -> l.onEntityAttached(entity));
        entity.getComponents().forEach(c -> onComponentAttached(entity, c));
    }

    /**
     * Detaches an entity
     * If the entity contains components, all corresponding components detachment events will be fired.
     * The entity object passed as parameter is not affected by this operation.
     */
    public Entity detachEntity(final EntityId entityId) {
        final Entity entity = entities.get(entityId);
        if (entity == null) {
            throw new IllegalArgumentException("Entity is not attached: " + entityId);
        }

        final List<Component> components = new LinkedList<>(entity.getComponents());
        Collections.reverse(components);
        components.forEach(c -> onComponentDetached(entity, c));
        entityLifecycleListeners.forEach(l -> l.onEntityDetached(entity));

        return entities.remove(entityId);
    }

    public void attachComponent(final EntityId entityId, final Component component) {
        final Entity entity = safeGetEntity(entityId);
        entity.attachComponent(component);
        onComponentAttached(entity, component);
    }

    public <T extends Component> T detachComponent(final EntityId entityId, final Class<T> clazz) {
        final Entity entity = safeGetEntity(entityId);
        final T component = entity.getComponent(clazz);
        if (component == null) {
            throw new IllegalArgumentException("Entity " + entityId + " contains o such component: " + clazz);
        }
        onComponentDetached(entity, component);
        entity.detachComponent(clazz);
        return component;
    }

    @Override
    public void registerEntityLifecycleListener(final EntityLifecycleListener listener) {
        entityLifecycleListeners.add(listener);
    }

    @Override
    public void deregisterEntityLifecycleListener(final EntityLifecycleListener listener) {
        entityLifecycleListeners.remove(listener);
    }

    @Override
    public <T extends Component> void registerComponentLifecycleListener(final ComponentLifecycleListener<T> listener, final Class<T> component) {
        componentLifecycleListeners.put(component, listener);
    }

    @Override
    public <T extends Component> void deregisterComponentLifecycleListener(final ComponentLifecycleListener<T> listener, final Class<T> component) {
        componentLifecycleListeners.remove(component, listener);
    }

    private Entity safeGetEntity(final EntityId entityId) {
        return Optional.ofNullable(entities.get(entityId)).orElseThrow(() -> new IllegalArgumentException("No such entity: " + entityId));
    }

    private void onComponentAttached(final Entity entity, final Component component) {
        componentLifecycleListeners.get(component.getClass()).forEach(l -> l.onComponentAttached(entity, component));
    }

    private void onComponentDetached(final Entity entity, final Component component) {
        componentLifecycleListeners.get(component.getClass()).forEach(l -> l.onComponentDetached(entity, component));
    }
}
