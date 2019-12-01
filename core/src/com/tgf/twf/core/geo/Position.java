package com.tgf.twf.core.geo;

import com.tgf.twf.core.ecs.Component;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * A {@link Component} indicating the position of an entity.
 */
@Data
@RequiredArgsConstructor
public final class Position {
    public final int x;
    public final int y;

    public static Position of(final int x, final int y) {
        return new Position(x, y);
    }

    public int manatthanDistance(final Position other) {
        return Math.abs(x - other.x) + Math.abs(other.x - other.y);
    }
}
