package com.tgf.twf.core.world.task;

import com.google.common.collect.ImmutableList;
import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.world.Building;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * A {@link Task} implementation to construct a {@link Building}.
 * Move the {@link Agent} to the construction site, perform a {@link ConstructAction}, and move it back to its {@link Agent}'s home.
 */
@RequiredArgsConstructor
public class ConstructTask implements Task {
    private final Building buildingComponent;
    private final Position buildingPosition;

    @Override
    public List<Action> createActions(final Component<Agent> agentStateComponent) {
        return ImmutableList.of(
                MoveActionFactory.create(agentStateComponent, agentStateComponent.getState().getHomePosition(), buildingPosition),
                new ConstructAction(buildingComponent),
                MoveActionFactory.create(agentStateComponent, buildingPosition, agentStateComponent.getState().getHomePosition())
        );
    }
}
