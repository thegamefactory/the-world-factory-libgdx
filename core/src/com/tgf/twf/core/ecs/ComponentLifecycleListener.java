package com.tgf.twf.core.ecs;

/**
 * Interface for a listener which is invoked when a component of type T is attached or detached from any entity.
 */
public interface ComponentLifecycleListener<StateT> {
    void onComponentAttached(final Component<StateT> component);
}
