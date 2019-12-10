package com.tgf.twf.core.world.agriculture;

import com.google.common.collect.ImmutableMap;
import com.tgf.twf.core.ecs.Entities;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.world.building.Building;
import com.tgf.twf.core.world.building.BuildingType;
import com.tgf.twf.core.world.task.TaskSystem;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * A system managing the lifecycle of {@link Field} components.
 */
public class AgricultureSystem implements System {
    private final List<Field> fields = new LinkedList<>();
    private final TaskSystem taskSystem;

    private final ImmutableMap<Class<? extends Field.State>, Function<Field, Field.State>> stateFactories;

    public AgricultureSystem(final TaskSystem taskSystem) {
        this.taskSystem = taskSystem;
        this.stateFactories = ImmutableMap.of(
                UncultivatedState.class, field -> new UncultivatedState(taskSystem, field.getRelatedComponent(Position.class).toVector2()),
                GrowingState.class, field -> new GrowingState(Duration.ofSeconds(2)),
                GrownState.class, field -> new GrownState(taskSystem, field.getRelatedComponent(Position.class).toVector2())
        );
        Entities.registerComponentEventListener(this::handle, Building.class, Building.ConstructedEvent.class);
    }

    public void handle(final Building sender, final Building.ConstructedEvent event) {
        if (sender.getBuildingType() == BuildingType.FIELD) {
            final UncultivatedState uncultivatedState = new UncultivatedState(taskSystem, sender.getRelatedComponent(Position.class).toVector2());
            final Field field = new Field(uncultivatedState);
            fields.add(field);
            sender.getEntity().attachComponent(field);
            uncultivatedState.onStateEnter();
        }
    }

    @Override
    public void update(final Duration delta) {
        for (final Field field : fields) {
            final Class<? extends Field.State> nextState = field.getState().tick(delta);
            if (nextState != null) {
                final Field.State state = stateFactories.getOrDefault(
                        nextState,
                        (f) -> {
                            throw new IllegalStateException("No state factory for " + String.valueOf(nextState));
                        }
                ).apply(field);
                field.setState(state);
                state.onStateEnter();
            }
        }
    }
}
