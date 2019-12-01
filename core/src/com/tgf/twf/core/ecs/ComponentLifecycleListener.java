package com.tgf.twf.core.ecs;

/**
 * Interface for a listener which is invoked when a component of type T is attached or detached from any entity managed by the
 * {@link EntityEventsProducer}.
 */
public interface ComponentLifecycleListener<T extends Component> {
    void onComponentAttached(final Entity entity, final T component);

    void onComponentDetached(final Entity entity, final T component);
}
