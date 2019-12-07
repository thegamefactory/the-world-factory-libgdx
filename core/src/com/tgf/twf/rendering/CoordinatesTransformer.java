package com.tgf.twf.rendering;

import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.geo.Vector2f;

/**
 * A converter between rendering position and world position.
 */
public class CoordinatesTransformer {
    private final Vector2f tileSize;
    private final Vector2f offset;
    private final Vector2f cameraPan;

    public static CoordinatesTransformer ofTileSize(final Vector2f tileSize) {
        return new CoordinatesTransformer(tileSize);
    }

    private CoordinatesTransformer(final Vector2f tileSize) {
        this.tileSize = new Vector2f(tileSize);
        this.offset = new Vector2f();
        this.cameraPan = new Vector2f();
    }

    public void setOffset(final float offsetX, final float offsetY) {
        this.offset.x = offsetX;
        this.offset.y = offsetY;
    }

    public void pan(final float deltaX, final float deltaY) {
        cameraPan.x = cameraPan.x + deltaX;
        cameraPan.y = cameraPan.y + deltaY;
    }

    public void centerCamera(final float worldX, final float worldY) {
        cameraPan.x = -((worldX + worldY) * tileSize.x) / 2;
        cameraPan.y = ((worldX - worldY) * tileSize.y) / 2;
    }

    public Vector2f getTileSize() {
        return new Vector2f(tileSize);
    }

    public void convertToScreen(final Vector2 input, final Vector2f output) {
        final float x = input.x;
        final float y = input.y;
        output.x = (x + y) * (tileSize.x / 2) + cameraPan.x;
        output.y = (-x + y) * (tileSize.y / 2) + cameraPan.y;
    }

    public void convertToWorld(final Vector2f input, final Vector2f output) {
        final float x = input.x;
        final float y = input.y;
        final float tmpX = (x - (offset.x + cameraPan.x)) / (tileSize.x);
        final float tmpY = ((offset.y + cameraPan.y) - y) / (tileSize.y);
        output.x = tmpX + tmpY;
        output.y = tmpX - tmpY;
    }
}
