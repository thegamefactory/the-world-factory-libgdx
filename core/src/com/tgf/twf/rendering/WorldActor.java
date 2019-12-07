package com.tgf.twf.rendering;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.tgf.twf.core.world.World;
import com.tgf.twf.input.WorldInputListener;

import java.time.Duration;

/**
 * An {@link Actor} that ticks the {@link World}, draws it with a {@link WorldDrawable} and processes inputs with a {@link WorldInputListener}.
 */
public class WorldActor extends Image {
    private final World world;

    public WorldActor(final World world, final WorldDrawable worldDrawable) {
        this.world = world;
        setDrawable(worldDrawable);
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
}
