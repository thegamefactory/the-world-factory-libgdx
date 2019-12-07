package com.tgf.twf.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tgf.twf.core.world.BuildingType;
import com.tgf.twf.core.world.PlayerIntentionApi;
import com.tgf.twf.rendering.BuildingTextures;
import lombok.RequiredArgsConstructor;

/**
 * Listens to click events on UI buttons for building tools and changes the active {@link Tool}.
 */
@RequiredArgsConstructor
public class BuildingToolButtonListener extends InputListener {
    private final WorldInputListener worldInputListener;
    private final BuildingType buildingType;
    private final PlayerIntentionApi playerIntentionApi;
    private final BuildingTextures buildingTextures;
    private final BuildTool buildTool;

    public BuildingToolButtonListener(final WorldInputListener worldInputListener,
                                      final BuildingType buildingType,
                                      final PlayerIntentionApi playerIntentionApi,
                                      final BuildingTextures buildingTextures) {
        this.worldInputListener = worldInputListener;
        this.buildingType = buildingType;
        this.playerIntentionApi = playerIntentionApi;
        this.buildingTextures = buildingTextures;
        this.buildTool = new BuildTool(playerIntentionApi, buildingType, buildingTextures.getBuildingTexture(buildingType));
    }

    @Override
    public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
        if (button != Input.Buttons.LEFT || pointer > 0) {
            return false;
        }
        worldInputListener.setActiveTool(buildTool);
        return true;
    }
}
