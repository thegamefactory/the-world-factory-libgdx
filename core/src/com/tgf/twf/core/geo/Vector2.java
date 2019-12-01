package com.tgf.twf.core.geo;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class Vector2 {
    public final int x;
    public final int y;

    public int manatthanDistance(final Vector2 other) {
        return Math.abs(x - other.x) + Math.abs(other.x - other.y);
    }
}
