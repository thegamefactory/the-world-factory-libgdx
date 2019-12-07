package com.tgf.twf.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.PlayerIntentionApi;
import com.tgf.twf.rendering.CoordinatesTransformer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Process player inputs and translates them into {@link PlayerIntentionApi} calls.
 */
@RequiredArgsConstructor
public class WorldInputListener extends InputListener {
    private final PlayerIntentionApi playerIntentionApi;
    private final CoordinatesTransformer coordinatesTransformer;
    private final ToolPreview toolPreview;

    @Getter
    private final Vector2f mouseScreen = new Vector2f();

    @Override
    public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
        if (button != Input.Buttons.LEFT || pointer > 0 || toolPreview.getTool() == null) {
            return false;
        }
        final Vector2f screen = new Vector2f(x, y);
        final Vector2 world = new Vector2();
        coordinatesTransformer.convertScreenToWorld(screen, world);
        return toolPreview.getTool().execute(world, ExecutionMode.COMMIT);
    }


    @Override
    public boolean mouseMoved(final InputEvent event, final float x, final float y) {
        mouseScreen.x = x;
        mouseScreen.y = y;
        toolPreview.setScreenPosition(mouseScreen);
        return false;
    }

    public void setActiveTool(final Tool tool) {
        toolPreview.setTool(tool);
    }
}
