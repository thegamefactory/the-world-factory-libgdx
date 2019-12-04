package com.tgf.twf;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.BuildingType;
import com.tgf.twf.core.world.PlayerIntentionApi;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Process player inputs and translates them into {@link PlayerIntentionApi} calls.
 */
@RequiredArgsConstructor
public class GameInputProcessor implements InputProcessor {
    private final PlayerIntentionApi playerIntentionApi;
    private final CoordinatesTransformer coordinatesTransformer;

    @Setter
    private int screenHeight;

    @Override
    public boolean keyDown(final int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(final int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(final char character) {
        return false;
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
        return false;
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
        if (button != Input.Buttons.LEFT || pointer > 0) {
            return false;
        }
        final Vector2 screen = new Vector2(screenX, screenHeight - screenY);
        final Vector2f world = new Vector2f();
        coordinatesTransformer.convertToWorld(screen, world);
        return playerIntentionApi.build(BuildingType.FIELD, Position.from(world));
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(final int amount) {
        return false;
    }
}
