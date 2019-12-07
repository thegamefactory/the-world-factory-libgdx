package com.tgf.twf.input;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.geo.Vector2f;

/**
 * Defines an interface for player actions which can be executed at the given position. These are typically mutations to the world.
 */
public interface Tool {
    boolean execute(Vector2 worldPosition, final ExecutionMode executionMode);

    void preview(Batch batch, Vector2f renderPos);

    Tool NULL_TOOL = new DefaultTool();

    final class DefaultTool implements Tool {

        @Override
        public boolean execute(final Vector2 worldPosition, final ExecutionMode executionMode) {
            return false;
        }

        @Override
        public void preview(final Batch batch, final Vector2f renderPos) {

        }
    }
}
