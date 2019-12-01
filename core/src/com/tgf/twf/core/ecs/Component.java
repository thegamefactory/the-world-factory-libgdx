package com.tgf.twf.core.ecs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Base interface for a component that can be attached to an entity.
 * Because a {@link Component} is identifier to {@link Entity} by its class, implementations of {@link Component} must be final classes.
 */
@RequiredArgsConstructor
public abstract class Component {
    @Getter
    private final Entity entity;

    public <T extends Component> T getRelatedComponent(final Class<T> clazz) {
        return entity.getComponent(clazz);
    }
}
