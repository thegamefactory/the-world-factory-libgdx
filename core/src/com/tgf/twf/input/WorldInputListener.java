package com.tgf.twf.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.PlayerIntentionApi;
import com.tgf.twf.rendering.CoordinatesTransformer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Process player inputs and translates them into {@link PlayerIntentionApi} calls.
 */
@RequiredArgsConstructor
public class WorldInputListener extends InputListener {
    private final PlayerIntentionApi playerIntentionApi;
    private final CoordinatesTransformer coordinatesTransformer;
    @Setter
    private Tool activeTool;

    @Getter
    private final Vector2f mouseWorld = new Vector2f();

    @Getter
    private final Vector2f mouseScreen = new Vector2f();

    @Override
    public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
        if (button != Input.Buttons.LEFT || pointer > 0 || activeTool == null) {
            return false;
        }
        final Vector2f screen = new Vector2f(x, y);
        final Vector2f world = new Vector2f();
        coordinatesTransformer.convertToWorld(screen, world);
        return activeTool.execute(Position.from(world));
    }


    @Override
    public boolean mouseMoved(final InputEvent event, final float x, final float y) {
        mouseScreen.x = x;
        mouseScreen.y = y;
        coordinatesTransformer.convertToWorld(mouseScreen, mouseWorld);
        return false;
    }
}
