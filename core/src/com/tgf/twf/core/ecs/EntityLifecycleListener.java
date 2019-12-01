package com.tgf.twf.core.ecs;

/**
 * Interface for a listener which is invoked when any entity is attached or detached from the corresponding {@link EntityEventsProducer}.
 */
public interface EntityLifecycleListener {
    void sendEntityAttachedEvent(final Entity entity);

    void onEntityDetached(final Entity entity);
}
