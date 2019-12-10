package com.tgf.twf.core.geo;

import com.tgf.twf.core.ecs.Component;
import lombok.Data;

/**
 * An immutable {@link Component} containing 2d coordinates.
 */
public final class Position extends Component implements Cloneable {
    public int x;
    public int y;

    public Position(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public Position(final Position other) {
        this.x = other.x;
        this.y = other.y;
    }

    public static Position from(final Vector2 position) {
        return new Position(position.x, position.y);
    }

    public Vector2 toVector2() {
        return new Vector2(x, y);
    }

    public void setPosition(final Vector2 newPosition) {
        if (newPosition.x != x || newPosition.y != y) {
            notify(new MoveEvent(newPosition));
        }
        this.x = newPosition.x;
        this.y = newPosition.y;
    }

    /**
     * {@link Component.Event} fired immediately before updating the state of a {@link Position} {@link Component}.
     */
    @Data
    public final static class MoveEvent implements Component.Event {
        private final Vector2 newPosition;
    }

    @Override
    public String toString() {
        return "position[" + getEntityId() + "](" + x + ";" + y + ")";
    }
}
