package com.tgf.twf.input;

import com.badlogic.gdx.graphics.g2d.Batch;
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
    private Vector2f worldPosition;
    private final CoordinatesTransformer coordinatesTransformer;

    public void preview(final Batch batch) {
        final Vector2f render = new Vector2f();
        coordinatesTransformer.convertWorldToRender(worldPosition, render);
        tool.preview(batch, render);
    }
}
