package com.tgf.twf.core.world.agriculture;

import com.google.common.collect.ImmutableList;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.util.Timer;
import com.tgf.twf.core.world.task.Action;
import com.tgf.twf.core.world.task.Agent;
import com.tgf.twf.core.world.task.MoveActionFactory;
import com.tgf.twf.core.world.task.Task;
import com.tgf.twf.core.world.task.TimedAction;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.List;

/**
 * A {@link Task} to harvest a {@link GrownState} {@link Field}.
 */
@RequiredArgsConstructor
public class HarvestTask implements Task {
    private final GrownState grownState;
    private final Vector2 fieldPosition;

    @Override
    public List<Action> createActions(final Agent agent) {
        return ImmutableList.of(
                MoveActionFactory.create(agent, agent.getHomePosition().toVector2(), fieldPosition, MoveActionFactory.MoveType.STANDARD),
                new TimedAction(new Timer(Duration.ofSeconds(2), grownState::complete), Action.Cost.FREE),
                MoveActionFactory.create(agent, fieldPosition, agent.getHomePosition().toVector2(), MoveActionFactory.MoveType.HOME)
        );
    }
}
