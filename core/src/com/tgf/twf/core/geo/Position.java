package com.tgf.twf.core.geo;

import lombok.Data;

/**
 * An immutable position data class to be used as entity component.
 */
@Data
public final class Position {
    public final int x;
    public final int y;

    public static Position from(final int x, final int y) {
        return new Position(x, y);
    }

    /**
     * Converts a {@link Vector2f} to the closest {@link Position}. Examples: {@code [-0.49;0.49] => [0,0], [-0.51,0,51] => [-1,1] }
     */
    public static Position from(final Vector2f position) {
        return new Position(Math.round(position.x), Math.round(position.y));
    }

    public int manatthanDistance(final Position other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }
}
