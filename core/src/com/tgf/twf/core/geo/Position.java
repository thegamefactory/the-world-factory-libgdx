package com.tgf.twf.core.geo;

import com.tgf.twf.core.ecs.Component;
import lombok.Data;

/**
 * An immutable {@link Component} containing 2d coordinates.
 */
public final class Position extends Component {
    public int x;
    public int y;

    public Position(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public static Position from(final int x, final int y) {
        return new Position(x, y);
    }

    public static Position from(final Vector2 position) {
        return new Position(position.x, position.y);
    }

    public static Position from(final Vector2f position) {
        return new Position(Math.round(position.x), Math.round(position.y));
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
    public final class MoveEvent implements Component.Event {
        private final Vector2 newPosition;
    }
}
