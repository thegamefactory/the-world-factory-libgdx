package com.tgf.twf.core.ecs;

/**
 * An interface for objects which are emitting events about the lifecyle of {@link Entity} objects as well as when {@link Component}s get
 * attached/detached to them.
 * This interface is implemented by the {@link EntityContainer} but in practice it is useful to extract this interface so that other objects
 * composing an {@link EntityContainer} can expose the same interface and delegate the implementation to the container.
 */
public interface EntityEventsProducer {
    void registerEntityLifecycleListener(final EntityLifecycleListener listener);

    void deregisterEntityLifecycleListener(final EntityLifecycleListener listener);

    <T extends Component> void registerComponentLifecycleListener(final ComponentLifecycleListener<T> listener, final Class<T> component);

    <T extends Component> void deregisterComponentLifecycleListener(final ComponentLifecycleListener<T> listener, final Class<T> component);
}
