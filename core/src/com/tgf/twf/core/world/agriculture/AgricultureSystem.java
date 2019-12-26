package com.tgf.twf.core.world.agriculture;

import com.google.common.collect.ImmutableMap;
import com.tgf.twf.core.ecs.Entities;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.world.agents.TaskSystem;
import com.tgf.twf.core.world.building.Building;
import com.tgf.twf.core.world.building.BuildingType;
import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.core.world.storage.Storage;

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
                UncultivatedState.class, field -> new UncultivatedState(taskSystem, field.getPosition()),
                GrowingState.class, field -> new GrowingState(Rules.FIELD_GROWING_DURATION),
                GrownState.class, field -> new GrownState(
                        taskSystem,
                        field.getPosition(),
                        field.getRelatedComponent(Storage.class))
        );
        Entities.registerComponentEventListener(this::handle, Building.class, Building.ConstructedEvent.class);
    }

    public void handle(final Building sender, final Building.ConstructedEvent event) {
        if (sender.getBuildingType() == BuildingType.FIELD) {
            final UncultivatedState uncultivatedState = new UncultivatedState(taskSystem, sender.getPosition());
            final Field field = new Field(uncultivatedState);
            fields.add(field);
            sender.getEntity().attachComponent(field);
            uncultivatedState.onStateEnter();
        }
    }

    @Override
    public void tick() {
        for (final Field field : fields) {
            final Class<? extends Field.State> nextState = field.getState().tick();
            if (nextState != null) {
                final Field.State state = stateFactories.getOrDefault(
                        nextState,
                        (f) -> {
                            throw new IllegalStateException("No state factory for " + String.valueOf(nextState));
                        }
                ).apply(field);
                field.setState(state);
                field.notify(Field.StateChangeEvent.INSTANCE);
                state.onStateEnter();
            }
        }
    }
}
