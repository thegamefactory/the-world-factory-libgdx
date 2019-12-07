package com.tgf.twf.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.LinkedList;
import java.util.List;

/**
 * Top level game input processor.
 * Intercepts all input and delegates them if not consumed.
 * Only handles escape key for now.
 */
public class GameInputProcessor implements InputProcessor {
    private final InputProcessor delegate;

    private final List<Integer> leftKeys = new LinkedList<>();
    private final List<Integer> rightKeys = new LinkedList<>();
    private final List<Integer> upKeys = new LinkedList<>();
    private final List<Integer> downKeys = new LinkedList<>();

    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;
    private boolean isUpPressed = false;
    private boolean isDownPressed = false;

    public int horizontalSpeed() {
        if (isLeftPressed && !isRightPressed) {
            return -1;
        }
        if (isRightPressed && !isLeftPressed) {
            return 1;
        }
        return 0;
    }

    public int verticalSpeed() {
        if (isUpPressed && !isDownPressed) {
            return 1;
        }
        if (isDownPressed && !isUpPressed) {
            return -1;
        }
        return 0;
    }

    public GameInputProcessor(final InputProcessor delegate) {
        this.delegate = delegate;
        this.leftKeys.add(Input.Keys.A);
        this.leftKeys.add(Input.Keys.LEFT);
        this.rightKeys.add(Input.Keys.D);
        this.rightKeys.add(Input.Keys.RIGHT);
        this.upKeys.add(Input.Keys.W);
        this.upKeys.add(Input.Keys.UP);
        this.downKeys.add(Input.Keys.S);
        this.downKeys.add(Input.Keys.DOWN);
    }

    @Override
    public boolean keyDown(final int keycode) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            return true;
        }
        return keyInput(keycode, true) || delegate.keyDown(keycode);
    }

    @Override
    public boolean keyUp(final int keycode) {
        return keyInput(keycode, false) || delegate.keyUp(keycode);
    }

    private boolean keyInput(final int keycode, final boolean isPressed) {
        if (leftKeys.contains(keycode)) {
            isLeftPressed = isPressed;
            return true;
        }
        if (rightKeys.contains(keycode)) {
            isRightPressed = isPressed;
            return true;
        }
        if (upKeys.contains(keycode)) {
            isUpPressed = isPressed;
            return true;
        }
        if (downKeys.contains(keycode)) {
            isDownPressed = isPressed;
            return true;
        }
        return false;
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