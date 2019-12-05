package com.tgf.twf;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.BuildingType;
import com.tgf.twf.core.world.PlayerIntentionApi;
import lombok.RequiredArgsConstructor;

/**
 * Process player inputs and translates them into {@link PlayerIntentionApi} calls.
 */
@RequiredArgsConstructor
public class WorldInputProcessor extends InputListener {
    private final PlayerIntentionApi playerIntentionApi;
    private final CoordinatesTransformer coordinatesTransformer;

    @Override
    public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
        if (button != Input.Buttons.LEFT || pointer > 0) {
            return false;
        }
        final Vector2f screen = new Vector2f(x, y);
        final Vector2f world = new Vector2f();
        coordinatesTransformer.convertToWorld(screen, world);
        return playerIntentionApi.build(BuildingType.FIELD, Position.from(world));

    }
}
