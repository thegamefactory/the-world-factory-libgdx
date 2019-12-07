package com.tgf.twf.input;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.BuildingType;
import com.tgf.twf.core.world.PlayerIntentionApi;
import com.tgf.twf.rendering.TransparentTexture;
import lombok.RequiredArgsConstructor;

/**
 * Tool used to construct buildings and render building previews.
 */
@RequiredArgsConstructor
public class BuildTool implements Tool {
    private final PlayerIntentionApi playerIntentionApi;
    private final BuildingType buildingType;
    private final TransparentTexture buildingTexture;

    @Override
    public boolean execute(final Position worldPosition) {
        return playerIntentionApi.build(buildingType, worldPosition);
    }

    @Override
    public void preview(final Batch batch, final Vector2f renderPos) {
        batch.setColor(1.0f, 1.0f, 1.0f, 0.7f);
        batch.draw(buildingTexture, renderPos.x, renderPos.y);
        batch.setColor(1.0f, 1.0f, 1.0f, 1f);
    }
}
