package com.tgf.twf.rendering;

import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.geo.Vector2f;
import lombok.Getter;

/**
 * A converter between rendering position and world position.
 */
public class CoordinatesTransformer {
    @Getter
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

    public void convertWorldToScreen(final Vector2 world, final Vector2f screen) {
        final float x = world.x;
        final float y = world.y;
        screen.x = (x + y) * (tileSize.x / 2) + cameraPan.x;
        screen.y = (-x + y) * (tileSize.y / 2) + cameraPan.y;
    }

    public void convertScreenToWorld(final Vector2f screen, final Vector2f world) {
        final float x = screen.x;
        final float y = screen.y;
        final float tmpX = (x - (offset.x + cameraPan.x)) / (tileSize.x);
        final float tmpY = ((offset.y + cameraPan.y) - y) / (tileSize.y);
        world.x = tmpX + tmpY;
        world.y = tmpX - tmpY;
    }

    public void convertScreenToWorld(final Vector2f screen, final Vector2 world) {
        final float x = screen.x;
        final float y = screen.y;
        final float tmpX = (x - (offset.x + cameraPan.x)) / (tileSize.x);
        final float tmpY = ((offset.y + cameraPan.y) - y) / (tileSize.y);
        world.x = Math.round(tmpX + tmpY);
        world.y = Math.round(tmpX - tmpY);
    }

    public void convertScreenToRender(final Vector2f screen, final Vector2f render) {
        render.x = screen.x - tileSize.x / 2;
        render.y = screen.y - tileSize.y / 2;
    }

    public void convertMouseToRender(final Vector2f mouse, final Vector2f render) {
        render.x = mouse.x - offset.x;
        render.y = mouse.y - offset.y;
    }

    public void convertWorldToRender(final Vector2 world, final Vector2f render) {
        final Vector2f screen = new Vector2f();
        convertWorldToScreen(world, screen);
        convertScreenToRender(screen, render);
    }

    public float convertWorldToScreenXWithoutOffset(final Vector2f vector) {
        return (vector.x + vector.y) * (tileSize.x / 2);
    }

    public float convertWorldToScreenYWithoutOffset(final Vector2f vector) {
        return (-vector.x + vector.y) * (tileSize.y / 2);
    }
}
