package com.tgf.twf.core.ecs;

import lombok.Getter;

/**
 * {@link Component} are data that can be attached to an {@link Entity}.
 * Multiple {@link Component}s can be attached to the same {@link Entity}.
 * {@link Component} should be pure data objects.
 * {@link Component}s are identified to their class, therefore component implementers should be final.
 * The {@link Component} contains an {@link Entity}; the {@link Entity} is set via {@link Entity#attachComponent(Component)} logic. It should not
 * be set manually.
 *
 * Because {@link Component.Event} are notified to listeners which listen to events of that {@link Component} class via exact class matching,
 * implementations of {@link Component} should be final.
 */
public abstract class Component {
    @Getter
    private Entity entity;

    public Component() {
    }

    public EntityId getEntityId() {
        return entity.getEntityId();
    }

    // package private
    void setEntity(final Entity entity) {
        this.entity = entity;
    }

    public <ComponentT extends Component> ComponentT getRelatedComponent(final Class<ComponentT> clazz) {
        return entity.getComponent(clazz);
    }

    /**
     * Notifies of the {@link Event} all the {@link EventListener}s listening to that class of {@link Component} and that class of {@link Event}.
     */
    public void notify(final Event event) {
        Entities.getInstance().notify(this, event);
    }

    /**
     * Base interface for any event.
     *
     * Because {@link Component.Event} are notified to listeners which listen to events of that {@link Event} class via exact class matching,
     * implementations should be final.
     */
    public interface Event {
    }

    /**
     * An {@link Event} generated when a component is created.
     */
    public static final class CreationEvent implements Event {
        private CreationEvent() {
        }

        static CreationEvent INSTANCE = new CreationEvent();
    }

    /**
     * An {@link Event} generated when a component is deleted.
     */
    public static final class DeletionEvent implements Event {
        private DeletionEvent() {
        }

        static DeletionEvent INSTANCE = new DeletionEvent();
    }

    /**
     * An interface that any class who want to be notified for {@link Event}s need to implement.
     * The listener must register itself to events via {@link Entities#registerComponentEventListener(EventListener, Class, Class)}
     *
     * Because {@link Component.Event} are notified to listeners which listen to events of {@link Event} class and {@link Component} via exact class
     * matching, listeners may register themselves multiple times if they need to listen to multiple {@link Event} and {@link Component} classes;
     * in that class they need to omit the generic ComponentT and EventT parameter and implement {@link #handle(Component, Event)} logic that is
     * able to process base {@link Component} and base {@link Event}, casting them if necessary.
     */
    @FunctionalInterface
    public interface EventListener<ComponentT extends Component, EventT extends Component.Event> {
        void handle(final ComponentT sender, final EventT event);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + this.entity.getEntityId() + "]";
    }
}
