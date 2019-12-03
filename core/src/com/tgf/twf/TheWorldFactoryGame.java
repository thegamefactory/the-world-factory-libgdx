package com.tgf.twf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.AgentState;
import com.tgf.twf.core.world.BuildingType;
import com.tgf.twf.core.world.PlayerIntentionApi;
import com.tgf.twf.core.world.World;
import com.tgf.twf.libgdx.BuildingTextureSystem;
import com.tgf.twf.libgdx.TransparentTexture;

import java.time.Duration;
import java.util.List;

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

    private BuildingTextureSystem buildingTextureSystem;

    public TheWorldFactoryGame(final World world) {
        this.world = world;
        this.playerIntentionApi = new PlayerIntentionApi(world);
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

        playerIntentionApi.build(BuildingType.FIELD, Position.of(2, 2));
        playerIntentionApi.build(BuildingType.FIELD, Position.of(2, 3));
        playerIntentionApi.build(BuildingType.FIELD, Position.of(3, 2));
        playerIntentionApi.build(BuildingType.FIELD, Position.of(3, 3));

        buildingTextureSystem = new BuildingTextureSystem(dirt, farm, field);
    }

    @Override
    public void render() {
        final Duration delta = Duration.ofNanos((long) (1_000_000_000 * Gdx.graphics.getDeltaTime()));
        world.update(delta);
        buildingTextureSystem.update(delta);

        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        final Vector2 worldSize = world.getSize();

        batch.begin();

        for (int y = worldSize.y - 1; y >= 0; --y) {
            for (int x = 0; x < worldSize.x; ++x) {
                final int xPixel = (x + y) * 45;
                final int yPixel = (y - x) * 27 + 200;

                batch.draw(imageAt(x, y), xPixel, yPixel);
                final List<Component<AgentState>> agents = world.getGeoMap().getAgentsAt(x, y);
                for (int i = 0; i < agents.size(); i++) {
                    final Texture agentTexture;
                    if (agents.get(i).getState().isIdle()) {
                        agentTexture = agentIdle;
                    } else {
                        agentTexture = agent;
                    }
                    batch.draw(agentTexture, xPixel + ((int) (agent.getWidth() * (i - 0.5))) + 45, (int) (agent.getHeight() * 0.5) + yPixel + 27);
                }
            }
        }

        batch.end();
    }

    public TransparentTexture imageAt(final int x, final int y) {
        return world.getGeoMap().getBuildingAt(x, y)
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
