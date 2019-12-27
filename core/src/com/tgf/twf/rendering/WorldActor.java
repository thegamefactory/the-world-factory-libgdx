package com.tgf.twf.rendering;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.tgf.twf.core.world.World;
import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.input.WorldInputListener;

/**
 * An {@link Actor} that ticks the {@link World}, draws it with a {@link WorldDrawable} and processes inputs with a {@link WorldInputListener}.
 */
public class WorldActor extends Image {
    private final World world;
    private float ellapsed = 0;

    public WorldActor(final World world, final WorldDrawable worldDrawable) {
        this.world = world;
        setDrawable(worldDrawable);
    }

    @Override
    public void act(final float delta) {
        super.act(delta);

        ellapsed += delta;
        while (ellapsed > Rules.TICKS_PERIOD) {
            ellapsed -= Rules.TICKS_PERIOD;
            this.world.tick();
        }
    }
}
