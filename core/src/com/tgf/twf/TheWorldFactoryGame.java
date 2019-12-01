package com.tgf.twf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tgf.twf.core.world.World;

import java.time.Duration;

public class TheWorldFactoryGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture img;
    private final World world;

    public TheWorldFactoryGame(final World world) {
        this.world = world;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
    }

    @Override
    public void render() {
        world.update(Duration.ofNanos((long) (1_000_000_000 * Gdx.graphics.getDeltaTime())));

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
