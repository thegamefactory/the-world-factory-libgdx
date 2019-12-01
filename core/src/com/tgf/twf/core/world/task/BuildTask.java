package com.tgf.twf.core.world.task;

import com.google.common.collect.ImmutableList;
import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.world.AgentState;
import com.tgf.twf.core.world.BuildingState;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BuildTask implements Task {
    private final BuildingState buildingComponent;
    private final Position buildingPosition;

    @Override
    public List<Action> createActions(final Component<AgentState> agentStateComponent) {
        return ImmutableList.of(
                MoveActionFactory.create(agentStateComponent, agentStateComponent.getState().getHomePosition(), buildingPosition),
                new BuildAction(buildingComponent),
                MoveActionFactory.create(agentStateComponent, buildingPosition, agentStateComponent.getState().getHomePosition())
        );
    }
}
