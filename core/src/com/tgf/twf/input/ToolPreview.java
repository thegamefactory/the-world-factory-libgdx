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
        final Vector2f renderPosition = new Vector2f();
        final Vector2 worldPosition = new Vector2();
        coordinatesTransformer.convertScreenToWorld(screenPosition, worldPosition);
        coordinatesTransformer.convertWorldToRender(worldPosition, renderPosition);
        final boolean isSuccess = tool.execute(worldPosition, ExecutionMode.DRY_RUN);
        if (isSuccess) {
            batch.setColor(0.3f, 1.0f, 0.3f, 0.8f);
        } else {
            batch.setColor(1.0f, 0.3f, 0.3f, 0.8f);
        }
        tool.preview(batch, renderPosition);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public Vector2 getWorldPosition() {
        final Vector2 worldPosition = new Vector2();
        coordinatesTransformer.convertScreenToWorld(screenPosition, worldPosition);
        return worldPosition;
    }
}
