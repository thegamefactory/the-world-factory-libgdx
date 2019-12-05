package com.tgf.twf;

import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.geo.Vector2f;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

/**
 * A converter between rendering position and world position.
 */
@RequiredArgsConstructor
@Builder
public class CoordinatesTransformer {
    private final Vector2f tileSize;
    private final Vector2f offset;

    public Vector2f getTileSize() {
        return new Vector2f(tileSize);
    }

    public void convertToScreen(final Vector2 input, final Vector2f output) {
        final float x = input.x;
        final float y = input.y;
        output.x = (x + y) * (tileSize.x / 2) + offset.x;
        output.y = (-x + y) * (tileSize.y / 2) + offset.y;
    }

    public void convertToWorld(final Vector2f input, final Vector2f output) {
        final float x = input.x;
        final float y = input.y;
        final float tmpX = (x - offset.x) / (tileSize.x);
        final float tmpY = (offset.y - y) / (tileSize.y);
        output.x = tmpX + tmpY;
        output.y = tmpX - tmpY;
    }
}
