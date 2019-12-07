package com.tgf.twf.input;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.BuildingType;
import com.tgf.twf.core.world.PlayerIntentionApi;
import com.tgf.twf.rendering.TransparentSprite;
import lombok.RequiredArgsConstructor;

/**
 * Tool used to construct buildings and render building previews.
 */
@RequiredArgsConstructor
public class BuildTool implements Tool {
    private final PlayerIntentionApi playerIntentionApi;
    private final BuildingType buildingType;
    private final TransparentSprite buildingSprite;

    @Override
    public boolean execute(final Vector2 worldPosition, final ExecutionMode executionMode) {
        return playerIntentionApi.build(buildingType, worldPosition, executionMode);
    }

    @Override
    public void preview(final Batch batch, final Vector2f renderPos) {
        batch.draw(buildingSprite.getSprite(), renderPos.x, renderPos.y);
    }
}
