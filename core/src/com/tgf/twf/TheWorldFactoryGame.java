package com.tgf.twf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.Entities;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.Building;
import com.tgf.twf.core.world.PlayerIntentionApi;
import com.tgf.twf.core.world.World;
import com.tgf.twf.core.world.task.Agent;
import com.tgf.twf.libgdx.BuildingAspectSystem;
import com.tgf.twf.libgdx.TransparentTexture;

import java.time.Duration;
import java.util.List;

/**
 * Entry point; loads assets, create systems responsible for rendering and input processing, implements game loop and ticks systems.
 */
public class TheWorldFactoryGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private final World world;
    private final PlayerIntentionApi playerIntentionApi;

    private TransparentTexture dirt;
    private TransparentTexture farm;
    private TransparentTexture field;
    private TransparentTexture grass;
    private Texture agent;
    private Texture agentIdle;

    private BitmapFont font;

    private BuildingAspectSystem buildingAspectSystem;

    private final CoordinatesTransformer coordinatesTransformer;

    private GameInputProcessor gameInputProcessor;

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
        font = new BitmapFont();

        batch = new SpriteBatch();
        dirt = new TransparentTexture("dirt_tile.png");
        farm = new TransparentTexture("farm_tile.png");
        field = new TransparentTexture("field_tile.png");
        grass = new TransparentTexture("grass_tile.png");
        agent = new Texture("agent.png");
        agentIdle = new Texture("agent_idle.png");

        buildingAspectSystem = new BuildingAspectSystem(dirt, farm, field);
        Entities.registerComponentLifecycleListener(buildingAspectSystem, Building.class);

        gameInputProcessor = new GameInputProcessor(playerIntentionApi, coordinatesTransformer);
        gameInputProcessor.setScreenHeight(Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(gameInputProcessor);
    }

    @Override
    public void resize(final int width, final int height) {
        gameInputProcessor.setScreenHeight(height);
    }

    @Override
    public void render() {
        final Duration delta = Duration.ofNanos((long) (1_000_000_000 * Gdx.graphics.getDeltaTime()));
        world.update(delta);
        buildingAspectSystem.update(delta);

        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        final Vector2 worldSize = world.getSize();

        batch.begin();

        final Vector2 pos = new Vector2();
        final Vector2f screenPos = new Vector2f();
        final Vector2f tileSize = coordinatesTransformer.getTileSize();

        for (pos.y = worldSize.y - 1; pos.y >= 0; --pos.y) {
            for (pos.x = 0; pos.x < worldSize.x; ++pos.x) {
                coordinatesTransformer.convertToScreen(pos, screenPos);

                batch.draw(
                        imageAt(pos),
                        screenPos.x - tileSize.x / 2,
                        screenPos.y - tileSize.y / 2);
                final List<Component<Agent>> agents = world.getGeoMap().getAgentsAt(pos.x, pos.y);
                for (int i = 0; i < agents.size(); i++) {
                    final Texture agentTexture;
                    if (agents.get(i).getState().isIdle()) {
                        agentTexture = agentIdle;
                    } else {
                        agentTexture = agent;
                    }
                    batch.draw(agentTexture,
                            screenPos.x + ((int) (agent.getWidth() * (i - 0.5))),
                            screenPos.y + (int) (agent.getHeight() * -0.5));
                }
            }
        }

        batch.end();
    }

    public TransparentTexture imageAt(final Vector2 pos) {
        return world.getGeoMap().getBuildingAt(pos.x, pos.y)
                .map(buildingStateComponent -> buildingStateComponent.getRelatedComponent(TransparentTexture.class))
                .map(Component::getState)
                .orElse(grass);
    }

    @Override
    public void dispose() {
        batch.dispose();
        dirt.dispose();
        farm.dispose();
        field.dispose();
        grass.dispose();
    }
}
