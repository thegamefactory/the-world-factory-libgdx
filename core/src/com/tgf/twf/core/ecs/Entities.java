package com.tgf.twf.core.ecs;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
    private final List<EntityLifecycleListener> entityLifecycleListeners = new LinkedList<>();
    private final Multimap<Class<?>, ComponentLifecycleListener> componentLifecycleListeners = LinkedHashMultimap.create();

    private static final Entities INSTANCE = new Entities();

    static Entities getInstance() {
        return INSTANCE;
    }

    private Entities() {
        // no-op
    }

    public static void registerEntityLifecycleListener(final EntityLifecycleListener listener) {
        getInstance().entityLifecycleListeners.add(listener);
    }

    public static <T> void registerComponentLifecycleListener(final ComponentLifecycleListener<T> listener, final Class<T> component) {
        getInstance().componentLifecycleListeners.put(component, listener);
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

        entityLifecycleListeners.forEach(l -> l.sendEntityAttachedEvent(entity));
        entity.getComponents().forEach(this::sendComponentAttachedEvent);
    }

    void attachComponent(final Component<?> component) {
        if (entities.containsKey(component.getEntity().getEntityId())) {
            sendComponentAttachedEvent(component);
        }
    }

    private <T> void sendComponentAttachedEvent(final Component<T> component) {
        componentLifecycleListeners.get(component.getStateClass()).forEach(l -> l.onComponentAttached(component));
    }
}
