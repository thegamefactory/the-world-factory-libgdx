package com.tgf.twf.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.tgf.twf.core.ecs.EntityContainer;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.World;

import java.util.Random;

/**
 * Guice DI for {@link World}.
 */
public class WorldModule extends AbstractModule {
    private final Vector2 worldSize = new Vector2(5, 5);

    @Provides
    @Singleton
    public World world(final EntityContainer entityContainer, final Random random) {
        return new World(entityContainer, worldSize, random);
    }

    @Provides
    @Singleton
    public EntityContainer entityContainer() {
        return new EntityContainer();
    }

    @Provides
    @Singleton
    public Random random() {
        return new Random();
    }
}
