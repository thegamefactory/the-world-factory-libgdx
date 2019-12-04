package com.tgf.twf.core.ecs;

import lombok.Getter;

/**
 * {@link Component} are data that can be attached to an {@link Entity}.
 * Multiple {@link Component}s can be attached to the same {@link Entity}.
 * A {@link Component} wraps a state of the generic type StateT. Component should be pure data objects.
 * {@link Component}s are identified to the entity by the StateT class; therefore, StateT should be final classes.
 */
public final class Component<StateT> {
    @Getter
    private final Entity entity;

    @Getter
    private StateT state;

    public Component(final Entity entity, final StateT state) {
        this.entity = entity;
        this.state = state;
    }

    public <T> Component<T> getRelatedComponent(final Class<T> clazz) {
        return entity.getComponent(clazz);
    }

    public void updateState(final StateT newState) {
        final StateT oldState = this.state;
        this.state = newState;
        Entities.getInstance().updateComponentState(this, oldState);
    }

    public Class<StateT> getStateClass() {
        return (Class<StateT>) state.getClass();
    }
}
