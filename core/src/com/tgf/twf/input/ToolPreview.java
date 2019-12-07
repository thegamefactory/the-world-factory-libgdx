package com.tgf.twf.input;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.rendering.CoordinatesTransformer;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A singleton which keeps track of the currently selected tool and renders
 * previews on the map before executing the active tool.
 */
@Data
@AllArgsConstructor
public class ToolPreview {
    private Tool tool;
    private Vector2f screenPosition;
    private final CoordinatesTransformer coordinatesTransformer;

    public void preview(final Batch batch) {
        final Vector2f render = new Vector2f();
        coordinatesTransformer.convertScreenToWorld(screenPosition, render);
        coordinatesTransformer.convertWorldToRender(render, render);
        tool.preview(batch, render);
    }

    public Vector2 getWorldPosition() {
        final Vector2 worldPosition = new Vector2();
        coordinatesTransformer.convertScreenToWorld(screenPosition, worldPosition);
        return worldPosition;
    }
}
