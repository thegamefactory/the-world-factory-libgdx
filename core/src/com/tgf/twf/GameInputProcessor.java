package com.tgf.twf;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import lombok.RequiredArgsConstructor;

/**
 * Top level game input processor.
 * Intercepts all input and delegates them if not consumed.
 * Only handles escape key for now.
 */
@RequiredArgsConstructor
public class GameInputProcessor implements InputProcessor {
    private final InputProcessor delegate;

    @Override
    public boolean keyDown(final int keycode) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            return true;
        }
        return delegate.keyDown(keycode);
    }

    @Override
    public boolean keyUp(final int keycode) {
        return delegate.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(final char character) {
        return delegate.keyTyped(character);
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
        return delegate.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
        return delegate.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
        return delegate.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY) {
        return delegate.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(final int amount) {
        return delegate.scrolled(amount);
    }
}
