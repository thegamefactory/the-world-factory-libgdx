package com.tgf.twf.core.world.task;

import com.google.common.collect.ImmutableList;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.Building;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * A {@link Task} implementation to construct a {@link Building}.
 * Move the {@link Agent} to the construction site, perform a {@link ConstructAction}, and move it back to its {@link Agent}'s home.
 */
@RequiredArgsConstructor
public class ConstructTask implements Task {
    private final Building building;
    private final Vector2 buildingPosition;

    @Override
    public List<Action> createActions(final Agent agent) {
        final Vector2 agentHomePos = agent.getHomePosition().toVector2();
        return ImmutableList.of(
                MoveActionFactory.create(agent, agentHomePos, buildingPosition, MoveActionFactory.MoveType.STANDARD),
                new ConstructAction(building),
                MoveActionFactory.create(agent, buildingPosition, agentHomePos, MoveActionFactory.MoveType.HOME)
        );
    }
}
