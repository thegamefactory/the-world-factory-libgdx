package com.tgf.twf.core.world.agriculture;

import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.task.Action;
import com.tgf.twf.core.world.task.Task;
import com.tgf.twf.core.world.task.TaskFactory;
import com.tgf.twf.core.world.task.TaskSystem;
import com.tgf.twf.core.world.task.TimedAction;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

/**
 * The {@link Field.State}  of that field when it's uncultivated.
 * The {@link Field} will remain in that {@link Field.State} until an agent performs an plant on the {@link Field}, at which point it
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
        taskSystem.addTask(buildPlantTask());
    }

    private Task buildPlantTask() {
        return TaskFactory.create(
                TimedAction.builder()
                        .name("plant")
                        .completionCallback(this::complete)
                        .duration(Duration.ofSeconds(2))
                        .cost(Action.Cost.ONE_FOOD)
                        .build(),
                fieldPosition);
    }

    void complete() {
        isComplete = true;
    }
}
