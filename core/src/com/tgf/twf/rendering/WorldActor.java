package com.tgf.twf.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import com.tgf.twf.core.world.World;
import com.tgf.twf.input.ToolPreview;
import com.tgf.twf.input.WorldInputListener;

import java.time.Duration;

/**
 * An {@link Actor} that ticks the {@link World}, draws it with a {@link WorldDrawable} and processes inputs with a {@link WorldInputListener}.
 */
public class WorldActor extends Image implements Disposable {
    private final World world;

    private final CoordinatesTransformer coordinatesTransformer;

    // TODO: make this a texture component of the agent entity
    private final Texture agent;
    private final Texture agentIdle;

    // TODO: terrain
    private final TransparentTexture grass;

    public WorldActor(final World world, final CoordinatesTransformer coordinatesTransformer, final ToolPreview toolPreview) {
        this.world = world;
        this.coordinatesTransformer = coordinatesTransformer;

        grass = new TransparentTexture("grass_tile.png");
        agent = new Texture("agent.png");
        agentIdle = new Texture("agent_idle.png");

        setDrawable(WorldDrawable.builder()
                .agent(agent)
                .agentIdle(agentIdle)
                .coordinatesTransformer(coordinatesTransformer)
                .grass(grass)
                .world(world)
                .toolPreview(toolPreview)
                .build());
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
    }

    @Override
    public void dispose() {
        this.agent.dispose();
        this.agentIdle.dispose();
        this.grass.dispose();
    }
}
