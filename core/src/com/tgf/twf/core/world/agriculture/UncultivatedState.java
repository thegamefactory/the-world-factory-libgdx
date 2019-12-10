package com.tgf.twf.core.world.agriculture;

import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.task.TaskSystem;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

/**
 * The {@link Field.State}  of that field when it's uncultivated.
 * The {@link Field} will remain in that {@link Field.State} until an agent performs an {@link HarvestTask} on the {@link Field}, at which point it
 * will transition back to {@link UncultivatedState}.
 */
@RequiredArgsConstructor
public class UncultivatedState implements Field.State {
    private boolean isComplete = false;
    private final TaskSystem taskSystem;
    private final Vector2 fieldPosition;

    @Override
    public Class<? extends Field.State> tick(final Duration delta) {
        return isComplete ? GrowingState.class : null;
    }

    @Override
    public void onStateEnter() {
        taskSystem.addTask(new PlantTask(this, fieldPosition));
    }

    void complete() {
        isComplete = true;
    }
}
