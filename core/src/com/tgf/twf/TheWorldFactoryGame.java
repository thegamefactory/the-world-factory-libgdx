package com.tgf.twf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.PlayerIntentionApi;
import com.tgf.twf.core.world.World;

/**
 * Entry point; loads assets, create systems responsible for rendering and input processing, implements game loop and ticks systems.
 */
public class TheWorldFactoryGame extends ApplicationAdapter {
    private final World world;
    private final PlayerIntentionApi playerIntentionApi;

    private final CoordinatesTransformer coordinatesTransformer;

    private WorldInputProcessor worldInputProcessor;
    private Stage gameStage;

    public TheWorldFactoryGame(final World world) {
        this.world = world;
        this.playerIntentionApi = new PlayerIntentionApi(world);
        this.coordinatesTransformer = CoordinatesTransformer.builder()
                .tileSize(new Vector2f(90, 54))
                .offset(new Vector2f(45, 200))
                .build();
    }

    @Override
    public void create() {
        worldInputProcessor = new WorldInputProcessor(playerIntentionApi, coordinatesTransformer);

        gameStage = new Stage();
        Gdx.input.setInputProcessor(gameStage);
        Gdx.input.setInputProcessor(new GameInputProcessor(gameStage));

        final WorldActor worldActor = new WorldActor(world, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        worldActor.addListener(worldInputProcessor);
        gameStage.addActor(worldActor);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        gameStage.act(Gdx.graphics.getDeltaTime());
        gameStage.draw();
    }

    @Override
    public void dispose() {
    }
}
