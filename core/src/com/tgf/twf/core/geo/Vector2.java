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

    public int manatthanDistance(final Vector2 other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }
}
