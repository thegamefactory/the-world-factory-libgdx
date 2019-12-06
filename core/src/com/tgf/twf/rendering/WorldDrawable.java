package com.tgf.twf.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.World;
import com.tgf.twf.core.world.task.Agent;
import lombok.Builder;

/**
 * A drawable that draws the world in the given {@link Batch}.
 */
@Builder
public class WorldDrawable extends BaseDrawable {
    private final World world;
    private final CoordinatesTransformer coordinatesTransformer;

    // TODO: make this a texture component of the agent entity
    private final Texture agent;
    private final Texture agentIdle;

    // TODO: terrain
    private final TransparentTexture grass;

    final int MAX_AGENTS_RENDERED_PER_TILE = 5;

    @Override
    public void draw(final Batch batch, final float x, final float y, final float width, final float height) {
        final Vector2 pos = new Vector2();
        final Vector2f screenPos = new Vector2f();
        final Vector2f tileSize = coordinatesTransformer.getTileSize();
        final Vector2 worldSize = world.getSize();

        final Agent[] agents = new Agent[MAX_AGENTS_RENDERED_PER_TILE];
        for (pos.y = worldSize.y - 1; pos.y >= 0; --pos.y) {
            for (pos.x = 0; pos.x < worldSize.x; ++pos.x) {
                coordinatesTransformer.convertToScreen(pos, screenPos);
                batch.draw(
                        imageAt(pos),
                        screenPos.x - tileSize.x / 2,
                        screenPos.y - tileSize.y / 2);
                world.getGeoMap().getAgentsAt(pos.x, pos.y, agents);
                for (int i = 0; i < agents.length && agents[i] != null; i++) {
                    final Texture agentTexture;
                    if (agents[i].isIdle()) {
                        agentTexture = agentIdle;
                    } else {
                        agentTexture = agent;
                    }
                    batch.draw(agentTexture,
                            screenPos.x + ((int) (agent.getWidth() * (i - 0.5))),
                            screenPos.y + (int) (agent.getHeight() * -0.5));
                }
            }
        }
    }

    private TransparentTexture imageAt(final Vector2 pos) {
        return world.getGeoMap().getBuildingAt(pos.x, pos.y)
                .map(building -> building.getRelatedComponent(TransparentTexture.Component.class))
                .map(TransparentTexture.Component::getTransparentTexture)
                .orElse(grass);
    }
}
