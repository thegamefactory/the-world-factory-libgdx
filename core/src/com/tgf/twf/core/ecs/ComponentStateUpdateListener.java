package com.tgf.twf.core.ecs;

/**
 * Interface for a listener which is invoked when the state of a {@link Component} of type StateT is updated via
 * {@link Component#updateState(Object)}.
 *
 * Note that if the StateT is not an immutable object, it can be mutated without this method being invoked.
 */
public interface ComponentStateUpdateListener<StateT> {
    void onComponentStateUpdated(final Component<StateT> component, final StateT oldState);
}
