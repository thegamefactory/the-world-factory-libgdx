package com.tgf.twf.core.geo;

import lombok.Data;

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
        this(other.x, other.y);
    }
}
