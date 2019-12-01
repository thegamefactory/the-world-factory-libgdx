package com.tgf.twf.core.ecs;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.Map;

/**
 * Container of {@link Entity}.
 * Manages Entities lifecycle, including attaching/detaching them from the container, attaching/detaching {@link Component}s from the entities, and
 * firing corresponding events.
 * {@link EntityLifecycleListener} and {@link ComponentLifecycleListener}s can subscribe/unsubscribe to events fired by the container.
 */
public class Entities {
    private int nextId = 0;
    private final Map<EntityId, Entity> entities = new HashMap<>();
    private final Multimap<Class<?>, ComponentLifecycleListener> componentLifecycleListeners = LinkedHashMultimap.create();
    private final Multimap<Class<?>, ComponentStateUpdateListener> componentStateUpdateListeners = LinkedHashMultimap.create();

    private static final Entities INSTANCE = new Entities();

    static Entities getInstance() {
        return INSTANCE;
    }

    private Entities() {
        // no-op
    }

    public static <StateT> void registerComponentLifecycleListener(
            final ComponentLifecycleListener<StateT> listener,
            final Class<StateT> component) {
        getInstance().componentLifecycleListeners.put(component, listener);
    }

    public static <StateT> void registerComponentStateUpdateListener(
            final ComponentStateUpdateListener<StateT> listener,
            final Class<StateT> component) {
        getInstance().componentStateUpdateListeners.put(component, listener);
    }

    EntityId allocateEntityId() {
        return new EntityId(nextId++);
    }

    /**
     * Attaches an entity
     * If the entity contains components, all corresponding components attachment events will be fired.
     * The entity object passed as parameter is not affected by this operation.
     */
    void attachEntity(final Entity entity) {
        if (entities.containsKey(entity.getEntityId())) {
            throw new IllegalArgumentException("Duplicate entity id: " + entity.getEntityId());
        }
        entities.put(entity.getEntityId(), entity);

        entity.getComponents().forEach(this::sendComponentAttachedEvents);
    }

    <StateT> void attachComponent(final Component<StateT> component) {
        if (isComponentEntityAttached(component)) {
            sendComponentAttachedEvents(component);
        }
    }

    private <StateT> void sendComponentAttachedEvents(final Component<StateT> component) {
        componentLifecycleListeners.get(component.getStateClass()).forEach(l -> l.onComponentAttached(component));
    }

    <StateT> void updateComponentState(final Component<StateT> component, final StateT oldState) {
        if (isComponentEntityAttached(component)) {
            sendComponentUpdatedEvents(component, oldState);
        }
    }

    private <StateT> void sendComponentUpdatedEvents(final Component<StateT> component, final StateT oldState) {
        componentStateUpdateListeners.get(component.getStateClass()).forEach(l -> l.onComponentStateUpdated(component, oldState));
    }

    private boolean isComponentEntityAttached(final Component<?> component) {
        return entities.containsKey(component.getEntity().getEntityId());
    }
}
