package com.tgf.twf.core.ecs;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Manages {@link Entity} lifecycle.
 * All active entities must be attached to {@link Entities} via the {@link #attachEntity(Entity)} method.
 * In practice {@link Entities} is a singleton and the creation of a new {@link Entity} automatically handles the attachment to {@link Entities}.
 */
public class Entities {
    private int nextId = 0;
    private final Map<EntityId, Entity> entities = new HashMap<>();
    private final Multimap<ComponentEventKey, Component.EventListener> componentEventListeners = LinkedHashMultimap.create();

    @Data
    private static class ComponentEventKey {
        private final Class<? extends Component> componentClass;
        private final Class<? extends Component.Event> eventClass;
    }

    private static final Entities INSTANCE = new Entities();

    static Entities getInstance() {
        return INSTANCE;
    }

    private Entities() {
        // no-op
    }

    /**
     * Registers a {@link Component.EventListener} which will be notified for all events of the given {@link Component} class and of the given
     * {@link Component.Event} class. The same {@link Component.EventListener} can be registered multiple times with different {@link Component}
     * and {@link Component.Event} classes.
     */
    public static <ComponentT extends Component, EventT extends Component.Event> void registerComponentEventListener(
            final Component.EventListener<ComponentT, EventT> listener,
            final Class<ComponentT> componentClass,
            final Class<EventT> eventClass) {
        if (Component.class.equals(componentClass)) {
            throw new IllegalArgumentException("Cannot register base component class. Use actual components implementations.");
        }
        if (Component.Event.class.equals(eventClass)) {
            throw new IllegalArgumentException("Cannot register base event class. Use actual event implementations.");
        }
        getInstance().componentEventListeners.put(new ComponentEventKey(componentClass, eventClass), listener);
    }

    public static <ComponentT extends Component> Stream<ComponentT> allComponents(final Class<ComponentT> componentStateClass) {
        return getInstance().entities.values().stream().map(e -> e.getComponent(componentStateClass)).filter(Objects::nonNull);
    }

    EntityId allocateEntityId() {
        return new EntityId(nextId++);
    }

    Entity getEntity(final EntityId entityId) {
        return entities.get(entityId);
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

        final List<Component> components = ImmutableList.copyOf(entity.getComponents());
        components.forEach(this::sendComponentAttachedEvents);
    }

    <ComponentT extends Component> void attachComponent(final ComponentT component) {
        if (isComponentEntityAttached(component)) {
            sendComponentAttachedEvents(component);
        }
    }

    private <ComponentT extends Component> boolean isComponentEntityAttached(final ComponentT component) {
        return entities.containsKey(component.getEntity().getEntityId());
    }

    private <ComponentT extends Component> void sendComponentAttachedEvents(final ComponentT component) {
        componentEventListeners.get(new ComponentEventKey(component.getClass(), Component.CreationEvent.class))
                .forEach(l -> l.handle(component, Component.CreationEvent.INSTANCE));
    }

    public boolean notify(final Component sender, final Component.Event event) {
        componentEventListeners.get(new ComponentEventKey(sender.getClass(), event.getClass()))
                .forEach(l -> l.handle(sender, event));
        return true;
    }
}
