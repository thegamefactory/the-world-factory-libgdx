package com.tgf.twf;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.PlayerIntentionApi;
import com.tgf.twf.core.world.World;
import com.tgf.twf.libgdx.BuildingAspectSystem;
import com.tgf.twf.libgdx.TransparentTexture;

import java.time.Duration;

/**
 * An {@link Actor} that ticks the {@link World}, draws it with a {@link WorldDrawable} and processes inputs with a {@link WorldInputProcessor}.
 */
public class WorldActor extends Image {
    private final World world;

    private final CoordinatesTransformer coordinatesTransformer;

    // TODO: add a Textures class to create a nicer handle on them and also ensure that they are disposed properly
    private final TransparentTexture dirt;
    private final TransparentTexture farm;
    private final TransparentTexture field;

    // TODO: make this a texture component of the agent entity
    private final Texture agent;
    private final Texture agentIdle;

    // TODO: terrain
    private final TransparentTexture grass;

    private final BuildingAspectSystem buildingAspectSystem;

    public WorldActor(final World world, final float sizeX, final float sizeY) {
        this.world = world;

        coordinatesTransformer = CoordinatesTransformer.builder()
                .tileSize(new Vector2f(90, 54))
                .offset(new Vector2f(45, 200))
                .build();

        dirt = new TransparentTexture("dirt_tile.png");
        farm = new TransparentTexture("farm_tile.png");
        field = new TransparentTexture("field_tile.png");

        buildingAspectSystem = new BuildingAspectSystem(dirt, farm, field);

        grass = new TransparentTexture("grass_tile.png");
        agent = new Texture("agent.png");
        agentIdle = new Texture("agent_idle.png");

        setDrawable(WorldDrawable.builder()
                .agent(agent)
                .agentIdle(agentIdle)
                .coordinatesTransformer(coordinatesTransformer)
                .grass(grass)
                .world(world)
                .build());
        setBounds(0, 0, sizeX, sizeY);

        addListener(new WorldInputProcessor(new PlayerIntentionApi(world), coordinatesTransformer));
    }

    @Override
    public void act(final float delta) {
        super.act(delta);

        final Duration deltaDuration = Duration.ofNanos((long) (1_000_000_000 * delta));

        // TODO: decompose the deltas in a series of constantly spaced ticks to ensure predictable behavior
        // TODO: for example, imagine that we define a world tick as 0.01s
        // TODO: then, if since the last update there was 0.02341384s, we would tick the world two times
        // TODO: this has nice properties that from the world point of view time is decomposed in discrete, constant intervals (1 tick)
        this.world.update(deltaDuration);
        buildingAspectSystem.update(deltaDuration);
    }
}
