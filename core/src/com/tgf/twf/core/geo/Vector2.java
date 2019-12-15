package com.tgf.twf.core.geo;

import lombok.Data;

/**
 * An integer vector.
 */
@Data
public class Vector2 {
    public int x;
    public int y;

    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(final Vector2 other) {
        this.x = other.x;
        this.y = other.y;
    }

    public static void minus(final Vector2 left, final Vector2 right, final Vector2 out) {
        out.x = left.x - right.x;
        out.y = left.y - right.y;
    }

    public boolean isInBounds(final Vector2 position) {
        return position.x >= 0 && position.x < this.x && position.y > 0 && position.y < this.y;
    }
}
