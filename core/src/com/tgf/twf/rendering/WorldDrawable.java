package com.tgf.twf.rendering;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.tgf.twf.core.geo.GeoMap;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.World;
import com.tgf.twf.core.world.agents.Agent;
import com.tgf.twf.core.world.building.Building;
import com.tgf.twf.core.world.storage.Storage;
import com.tgf.twf.core.world.terrain.TerrainType;
import com.tgf.twf.input.ToolPreview;
import com.tgf.twf.input.ToolTip;
import lombok.Builder;

import java.util.List;

/**
 * A drawable that draws the world in the given {@link Batch}.
 */
public class WorldDrawable extends BaseDrawable {
    private final GeoMap geoMap;
    private final CoordinatesTransformer coordinatesTransformer;

    private final RenderableTilesTraverser renderableTilesTraverser;

    private final Sprite agentSprite;
    private final Sprite agentIdleSprite;
    private final Sprite agentCarryingSprite;
    private final TransparentSprite[] terrainSprites;

    private final ToolPreview toolPreview;
    private final ToolTip toolTip;

    final int MAX_AGENTS_RENDERED_PER_TILE = 5;

    final Vector2 worldSize;
    final Vector2 pos = new Vector2();
    final Vector2f screenPos = new Vector2f();
    final Vector2f renderPos = new Vector2f();

    @Builder
    public WorldDrawable(
            final World world,
            final CoordinatesTransformer coordinatesTransformer,
            final ToolPreview toolPreview,
            final ToolTip toolTip,
            final TextureAtlas textureAtlas) {
        this.geoMap = world.getGeoMap();
        this.worldSize = world.getSize();
        this.coordinatesTransformer = coordinatesTransformer;
        this.toolPreview = toolPreview;
        this.toolTip = toolTip;
        this.agentSprite = textureAtlas.createSprite("agent");
        this.agentIdleSprite = textureAtlas.createSprite("agent_idle");
        this.agentCarryingSprite = textureAtlas.createSprite("agent_carrying");
        this.terrainSprites = new TransparentSprite[TerrainType.values().length];
        for (final TerrainType terrainType : TerrainType.values()) {
            terrainSprites[terrainType.ordinal()] = new TransparentSprite(textureAtlas.createSprite(terrainType.getName() + "_tile"));
        }
        this.renderableTilesTraverser = RenderableTilesTraverser.builder()
                .coordinatesTransformer(coordinatesTransformer)
                .worldSize(worldSize)
                .build();
    }

    @Override
    public void draw(final Batch batch, final float x, final float y, final float width, final float height) {
        drawTerrain(batch);
        drawBuildingAndAgents(batch);
        toolTip.render(batch);
    }

    private void drawTerrain(final Batch batch) {
        this.renderableTilesTraverser.forEach((pos) -> {
            if (!geoMap.isBuildingAt(pos)) {
                final TerrainType terrainType = geoMap.getTerrainAt(pos);
                coordinatesTransformer.convertWorldToScreen(pos, screenPos);
                coordinatesTransformer.convertScreenToRender(screenPos, renderPos);
                batch.draw(terrainSprites[terrainType.ordinal()].getSprite(), renderPos.x, renderPos.y);
            }
        });
    }

    private void drawBuildingAndAgents(final Batch batch) {
        final Vector2 toolPreviousPos = toolPreview.getWorldPosition();
        this.renderableTilesTraverser.forEach((pos) ->
                {
                    final Building building = geoMap.getBuildingAt(pos);
                    final List<Agent> agents = geoMap.getAgentsAt(pos.x, pos.y);
                    final boolean isToolPreviewPos = toolPreviousPos.equals(pos);

                    if (building == null && agents.size() == 0 && !isToolPreviewPos) {
                        return;
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
        );
    }

    private void drawBuilding(final Batch batch, final Building building) {
        coordinatesTransformer.convertScreenToRender(screenPos, renderPos);
        final Sprite buildingImage = building.getRelatedComponent(TransparentSprite.Component.class).getSprite();
        batch.draw(buildingImage, renderPos.x, renderPos.y);
    }

    private void drawAgents(final Batch batch, final List<Agent> agents) {
        int idleAgentCount = 0;
        for (final Agent agent : agents) {
            // TODO: implement 1:1 agent state - sprite mapping
            if (agent.isIdle()) {
                idleAgentCount++;
                continue;
            }
            final Vector2f subTilePosition = agent.getSubTilePosition();
            final boolean isCarrying = !agent.getRelatedComponent(Storage.class).isEmpty();

            batch.draw(isCarrying ? agentCarryingSprite : agentSprite,
                    screenPos.x + ((int) (agentSprite.getWidth() * -0.5)) + coordinatesTransformer.convertWorldToScreenXWithoutOffset(subTilePosition),
                    screenPos.y + (int) (agentSprite.getHeight() * -0.5) + coordinatesTransformer.convertWorldToScreenYWithoutOffset(subTilePosition));
        }
        for (int i = 0; i < idleAgentCount; i++) {
            batch.draw(agentIdleSprite,
                    screenPos.x + ((int) (agentSprite.getWidth() * (i - 0.5))),
                    screenPos.y + (int) (agentSprite.getHeight() * -0.5));
        }
    }
}
