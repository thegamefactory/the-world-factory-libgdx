package com.tgf.twf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.AgentState;
import com.tgf.twf.core.world.BuildingState;
import com.tgf.twf.core.world.BuildingType;
import com.tgf.twf.core.world.PlayerIntentionApi;
import com.tgf.twf.core.world.World;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class TheWorldFactoryGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private final World world;
    private final PlayerIntentionApi playerIntentionApi;

    private Texture dirt;
    private Texture farm;
    private Texture field;
    private Texture grass;
    private Texture agent;

    public TheWorldFactoryGame(final World world) {
        this.world = world;
        this.playerIntentionApi = new PlayerIntentionApi(world);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        dirt = new Texture("dirt.png");
        farm = new Texture("farm.png");
        field = new Texture("field.png");
        grass = new Texture("grass.png");
        agent = new Texture("agent.png");
        playerIntentionApi.build(BuildingType.FIELD, Position.of(2, 2));
    }

    @Override
    public void render() {
        world.update(Duration.ofNanos((long) (1_000_000_000 * Gdx.graphics.getDeltaTime())));

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        final int tileSizePixels = 64;
        final Vector2 worldSize = world.getSize();

        batch.begin();
        for (int x = 0; x < worldSize.x; ++x) {
            for (int y = 0; y < worldSize.y; ++y) {
                final int xPixel = x * tileSizePixels;
                final int yPixel = y * tileSizePixels;

                batch.draw(imageAt(x, y), xPixel, yPixel);
                final List<Component<AgentState>> agents = world.getGeoMap().getAgentsAt(x, y);
                for (int i = 0; i < agents.size(); i++) {
                    batch.draw(agent, xPixel + (agent.getWidth() * i), yPixel);
                }
            }
        }

        batch.end();
    }

    public Texture imageAt(final int x, final int y) {
        final Optional<Component<BuildingState>> buildingComponentOptional = world.getGeoMap().getBuildingAt(x, y);
        if (buildingComponentOptional.isPresent()) {
            final BuildingState buildingComponent = buildingComponentOptional.get().getState();
            final BuildingType buildingType = buildingComponent.getBuildingType();
            switch (buildingType) {
                case FARM:
                    return farm;
                case FIELD:
                    return buildingComponent.isBuilt() ? field : dirt;
                default:
                    throw new IllegalStateException("Not a valid building type: " + buildingType);
            }
        } else {
            return grass;
        }
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
