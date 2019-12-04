package com.tgf.twf.core.ecs;

/**
 * Interface for a listener which is invoked when a {@link Component} of type StateT is attached to any {@link Entity}.
 */
public interface ComponentLifecycleListener<StateT> {
    void onComponentAttached(final Component<StateT> component);
}
