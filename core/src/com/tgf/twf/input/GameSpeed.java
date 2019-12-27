package com.tgf.twf.input;

/**
 * A small data class to model the game speed
 */
public class GameSpeed {
    private boolean paused = false;
    private int speedFactor = 0;

    void decreaseSpeed() {
        speedFactor--;
    }

    void increaseSpeed() {
        speedFactor++;
    }

    void togglePause() {
        paused = !paused;
    }

    public float getSpeedFactor() {
        return paused ? 0f : (float) Math.pow(2, speedFactor);
    }
}
