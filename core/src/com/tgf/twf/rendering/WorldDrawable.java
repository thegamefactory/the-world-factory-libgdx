package com.tgf.twf.rendering;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.World;
import com.tgf.twf.core.world.building.Building;
import com.tgf.twf.core.world.task.Agent;
import com.tgf.twf.input.ToolPreview;
import lombok.Builder;

/**
 * A drawable that draws the world in the given {@link Batch}.
 */
public class WorldDrawable extends BaseDrawable implements Disposable {
    private final World world;
    private final CoordinatesTransformer coordinatesTransformer;

    @Builder
    public WorldDrawable(
            final World world,
            final CoordinatesTransformer coordinatesTransformer,
            final ToolPreview toolPreview,
            final TextureAtlas textureAtlas) {
        this.world = world;
        this.coordinatesTransformer = coordinatesTransformer;
        this.toolPreview = toolPreview;
        this.agent = textureAtlas.createSprite("agent");
        this.agentIdle = textureAtlas.createSprite("agent_idle");
        this.grass = new TransparentSprite(textureAtlas.createSprite("grass_tile"));
    }

    private final Sprite agent;
    private final Sprite agentIdle;
    private final TransparentSprite grass;

    private final ToolPreview toolPreview;

    final int MAX_AGENTS_RENDERED_PER_TILE = 5;

    Vector2 worldSize;
    final Vector2 pos = new Vector2();
    final Vector2f screenPos = new Vector2f();
    final Vector2f renderPos = new Vector2f();

    @Override
    public void draw(final Batch batch, final float x, final float y, final float width, final float height) {
        worldSize = world.getSize();
        drawTerrain(batch);
        drawBuildingAndAgents(batch);
    }

    private void drawTerrain(final Batch batch) {
        final Sprite grassSprite = grass.getSprite();
        for (pos.y = worldSize.y - 1; pos.y >= 0; --pos.y) {
            for (pos.x = 0; pos.x < worldSize.x; ++pos.x) {
                if (!world.getGeoMap().isPositionOccupied(pos)) {
                    coordinatesTransformer.convertWorldToScreen(pos, screenPos);
                    coordinatesTransformer.convertScreenToRender(screenPos, renderPos);
                    batch.draw(grassSprite, renderPos.x, renderPos.y);
                }
            }
        }
    }

    private void drawBuildingAndAgents(final Batch batch) {
        final Vector2 toolPreviousPos = toolPreview.getWorldPosition();
        final Agent[] agents = new Agent[MAX_AGENTS_RENDERED_PER_TILE];
        for (pos.y = worldSize.y - 1; pos.y >= 0; --pos.y) {
            for (pos.x = 0; pos.x < worldSize.x; ++pos.x) {
                final Building building = world.getGeoMap().getBuildingAt(pos);
                final int agentCount = world.getGeoMap().getAgentsAt(pos.x, pos.y, agents);
                final boolean isToolPreviewPos = toolPreviousPos.equals(pos);

                if (building == null && agentCount == 0 && !isToolPreviewPos) {
                    continue;
                }

                coordinatesTransformer.convertWorldToScreen(pos, screenPos);

                if (building != null) {
                    drawBuilding(batch, building);
                }

                drawAgents(batch, agents);

                if (isToolPreviewPos) {
                    toolPreview.preview(batch);
                }
            }
        }
    }

    private void drawBuilding(final Batch batch, final Building building) {
        coordinatesTransformer.convertScreenToRender(screenPos, renderPos);
        final Sprite buildingImage = building.getRelatedComponent(TransparentSprite.Component.class).getSprite();
        batch.draw(buildingImage, renderPos.x, renderPos.y);
    }

    private void drawAgents(final Batch batch, final Agent[] agents) {
        int idleAgentCount = 0;
        for (int i = 0; i < agents.length && agents[i] != null; i++) {
            if (agents[i].isIdle()) {
                idleAgentCount++;
                continue;
            }
            final Vector2f subTilePosition = agents[i].getSubTilePosition();
            batch.draw(agent,
                    screenPos.x + ((int) (agent.getWidth() * -0.5)) + coordinatesTransformer.convertVectorToScreenX(subTilePosition),
                    screenPos.y + (int) (agent.getHeight() * -0.5) + coordinatesTransformer.convertVectorToScreenY(subTilePosition));
        }
        for (int i = 0; i < idleAgentCount; i++) {
            batch.draw(agentIdle,
                    screenPos.x + ((int) (agent.getWidth() * (i - 0.5))),
                    screenPos.y + (int) (agent.getHeight() * -0.5));
        }
    }

    @Override
    public void dispose() {
        this.grass.getSprite().getTexture().dispose();
        this.agent.getTexture().dispose();
        this.agentIdle.getTexture().dispose();
    }
}
