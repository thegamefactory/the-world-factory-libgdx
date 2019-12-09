package com.tgf.twf.core.world.agriculture;

import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.task.TaskSystem;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
public class GrownState implements Field.State {
    private boolean isComplete = false;
    private final TaskSystem taskSystem;
    private final Vector2 fieldPosition;

    @Override
    public Class<? extends Field.State> tick(final Duration delta) {
        return isComplete ? UncultivatedState.class : this.getClass();
    }

    @Override
    public void onStateEnter() {
        taskSystem.addTask(new HarvestTask(this, fieldPosition));
    }

    void complete() {
        isComplete = true;
    }
}
