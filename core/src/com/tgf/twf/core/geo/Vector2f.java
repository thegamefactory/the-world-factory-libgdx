package com.tgf.twf.core.geo;

import lombok.Data;

/**
 * A float vector.
 */
@Data
public class Vector2f {
    public float x;
    public float y;

    public Vector2f() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2f(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(final Vector2f other) {
        this(other.x, other.y);
    }
}
