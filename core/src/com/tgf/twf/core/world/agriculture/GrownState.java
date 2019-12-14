package com.tgf.twf.core.world.agriculture;

import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.storage.Storage;
import com.tgf.twf.core.world.task.Agent;
import com.tgf.twf.core.world.task.Task;
import com.tgf.twf.core.world.task.TaskFactory;
import com.tgf.twf.core.world.task.TaskSystem;
import com.tgf.twf.core.world.task.TimedAction;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

/**
 * The {@link Field.State}  of that field when it's grown.
 * The {@link Field} will remain in that {@link Field.State} until an agent performs a harvest task on the {@link Field}, at which point it
 * will transition back to {@link UncultivatedState}.
 */
@RequiredArgsConstructor
public class GrownState implements Field.State {
    private boolean isComplete = false;
    private final TaskSystem taskSystem;
    private final Vector2 fieldPosition;

    @Override
    public Class<? extends Field.State> tick(final Duration delta) {
        return isComplete ? UncultivatedState.class : null;
    }

    @Override
    public void onStateEnter() {
        taskSystem.addTask(buildHarvestTask());
    }

    private Task buildHarvestTask() {
        return TaskFactory.create(
                (agent) -> TimedAction.builder()
                        .name("harvest")
                        .completionCallback(() -> this.complete(agent))
                        .duration(Rules.HARVEST_DURATION)
                        .cost(Rules.HARVEST_COST)
                        .build(),
                fieldPosition);
    }

    void complete(final Agent agent) {
        isComplete = true;
        if (!agent.getRelatedComponent(Storage.class).store(ResourceType.FOOD, Rules.FIELD_YIELD)) {
            throw new IllegalStateException("Agent rejected harvest");
        }
    }

    @Override
    public String toString() {
        return "GrownState[isComplete=" + isComplete + "]";
    }
}
