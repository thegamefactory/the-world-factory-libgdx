package com.tgf.twf.core.ecs;

import lombok.Getter;

/**
 * Base interface for a component that can be attached to an entity.
 * Because a {@link Component} is identifier to {@link Entity} by its class, implementations of {@link Component} must be final classes.
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
