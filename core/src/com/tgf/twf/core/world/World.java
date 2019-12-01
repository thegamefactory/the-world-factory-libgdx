package com.tgf.twf.core.world;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.ComponentLifecycleListener;
import com.tgf.twf.core.ecs.Entity;
import com.tgf.twf.core.ecs.EntityContainer;
import com.tgf.twf.core.ecs.EntityEventsProducer;
import com.tgf.twf.core.ecs.EntityFactory;
import com.tgf.twf.core.ecs.EntityLifecycleListener;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.geo.GeoMap;
import com.tgf.twf.core.geo.PositionComponent;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.task.TaskSystem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

/**
 * Top class representing the world.
 */
@RequiredArgsConstructor
public class World implements EntityEventsProducer, System {
    @Getter
    private final Vector2 size;
    private final Random random;

    @Getter
    private final GeoMap geoMap;

    private final EntityFactory entityFactory = new EntityFactory();
    private final EntityContainer entityContainer;
    @Getter
    private final TaskSystem taskSystem;

    public World(final EntityContainer entityContainer, final Vector2 size, final Random random) {
        this.entityContainer = entityContainer;
        this.size = size;
        this.random = random;

        geoMap = new GeoMap(size);
        entityContainer.registerComponentLifecycleListener(geoMap, PositionComponent.class);

        taskSystem = new TaskSystem();
        entityContainer.registerComponentLifecycleListener(taskSystem, AgentComponent.class);
    }

    public Entity createEntity(final Component... components) {
        final Entity entity = entityFactory.create();
        Arrays.stream(components).forEach(c -> entityContainer.attachComponent(entity.getEntityId(), c));
        return entity;
    }

    public void attachComponents(final Component... components) {
        Arrays.stream(components).forEach(c -> entityContainer.attachComponent(c.getEntity().getEntityId(), c));
    }

    @Override
    public void registerEntityLifecycleListener(final EntityLifecycleListener listener) {
        entityContainer.registerEntityLifecycleListener(listener);
    }

    @Override
    public void deregisterEntityLifecycleListener(final EntityLifecycleListener listener) {
        entityContainer.deregisterEntityLifecycleListener(listener);
    }

    @Override
    public <T extends Component> void registerComponentLifecycleListener(final ComponentLifecycleListener<T> listener, final Class<T> component) {
        entityContainer.registerComponentLifecycleListener(listener, component);
    }

    @Override
    public <T extends Component> void deregisterComponentLifecycleListener(final ComponentLifecycleListener<T> listener, final Class<T> component) {
        entityContainer.deregisterComponentLifecycleListener(listener, component);
    }

    @Override
    public void update(final Duration delta) {
        taskSystem.update(delta);
    }
}
