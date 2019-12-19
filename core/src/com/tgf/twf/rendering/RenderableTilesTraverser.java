package com.tgf.twf.rendering;

import com.badlogic.gdx.Gdx;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.geo.Vector2f;
import lombok.Builder;

/**
 * Provides a {@link #forEach(TileCallback)} method that invokes the callback for each tile whose position is within the screen bounds, in an
 * z-buffer order suitable for rendering.
 */
public class RenderableTilesTraverser {
    private final Vector2 worldSize;
    private final CoordinatesTransformer coordinatesTransformer;

    private final Vector2f screen = new Vector2f();
    private final Vector2 topRight = new Vector2();
    private final Vector2 position = new Vector2();

    @Builder
    public RenderableTilesTraverser(final Vector2 worldSize, final CoordinatesTransformer coordinatesTransformer) {
        this.worldSize = worldSize;
        this.coordinatesTransformer = coordinatesTransformer;
    }

    public void forEach(final TileCallback tileCallback) {
        final Vector2f tileSize = coordinatesTransformer.getTileSize();

        final int width = Gdx.graphics.getWidth() + (int) tileSize.x;
        final int height = Gdx.graphics.getHeight() + (int) tileSize.y;
        screen.x = width - tileSize.x / 2;
        screen.y = height - tileSize.y / 2;
        coordinatesTransformer.convertScreenToWorld(screen, topRight);

        final int numberOfRows = (int) Math.ceil(height / tileSize.y);
        final int numberOfColumns = (int) Math.ceil(width / tileSize.x);

        for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
            for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
                position.x = topRight.x - 1 - columnIndex + rowIndex;
                position.y = topRight.y - columnIndex - rowIndex;
                if (worldSize.isInBounds(position)) {
                    tileCallback.run(position);
                }
                position.x = topRight.x - columnIndex + rowIndex;
                position.y = topRight.y - columnIndex - rowIndex;
                if (worldSize.isInBounds(position)) {
                    tileCallback.run(position);
                }
            }
        }
    }

    @FunctionalInterface
    public interface TileCallback {
        void run(final Vector2 position);
    }
}
