package com.tgf.twf.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

/**
 * Top level game input processor.
 * Intercepts all input and delegates them if not consumed.
 * Only handles escape key for now.
 */
public class GameInputProcessor implements InputProcessor {
    private final InputProcessor delegate;
    private final ToolPreview toolPreview;
    private final ToolTip toolTip;

    private final List<Integer> leftKeys = new LinkedList<>();
    private final List<Integer> rightKeys = new LinkedList<>();
    private final List<Integer> upKeys = new LinkedList<>();
    private final List<Integer> downKeys = new LinkedList<>();

    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;
    private boolean isUpPressed = false;
    private boolean isDownPressed = false;

    @Getter
    private final GameSpeed gameSpeed = new GameSpeed();

    public GameInputProcessor(final InputProcessor delegate, final ToolPreview toolPreview, final ToolTip toolTip) {
        this.delegate = delegate;
        this.toolPreview = toolPreview;
        this.toolTip = toolTip;
        this.leftKeys.add(Input.Keys.A);
        this.leftKeys.add(Input.Keys.LEFT);
        this.rightKeys.add(Input.Keys.D);
        this.rightKeys.add(Input.Keys.RIGHT);
        this.upKeys.add(Input.Keys.W);
        this.upKeys.add(Input.Keys.UP);
        this.downKeys.add(Input.Keys.S);
        this.downKeys.add(Input.Keys.DOWN);
    }

    public int getHorizontalSpeed() {
        if (isLeftPressed && !isRightPressed) {
            return 1;
        }
        if (isRightPressed && !isLeftPressed) {
            return -1;
        }
        return 0;
    }

    public int getVerticalSpeed() {
        if (isUpPressed && !isDownPressed) {
            return -1;
        }
        if (isDownPressed && !isUpPressed) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean keyDown(final int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            if (Tool.NULL_TOOL.equals(toolPreview.getTool())) {
                Gdx.app.exit();
            } else {
                toolPreview.setTool(Tool.NULL_TOOL);
            }
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
        if (keycode == Input.Keys.CONTROL_LEFT) {
            toolTip.setIntrospectionKeyModifierPressed(isPressed);
            return true;
        }
        if (isPressed) {
            if (keycode == Input.Keys.P) {
                gameSpeed.togglePause();
                return true;
            }
            if (keycode == Input.Keys.valueOf("]") || keycode == Input.Keys.PLUS) {
                gameSpeed.increaseSpeed();
                return true;
            }
            if (keycode == Input.Keys.valueOf("[") || keycode == Input.Keys.MINUS) {
                gameSpeed.decreaseSpeed();
                return true;
            }
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
