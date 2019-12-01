package com.tgf.twf.core.geo;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * A {@link Component} indicating the position of an entity.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class PositionComponent extends Component {
    private Vector2 position;

    public PositionComponent(
            final Entity entity,
            @NonNull final Vector2 position) {
        super(entity);
        this.position = position;
    }

    public void setPosition(final Vector2 position) {
        this.position = position;
    }

    public int manatthanDistance(final PositionComponent other) {
        return this.position.manatthanDistance(other.position);
    }
}
