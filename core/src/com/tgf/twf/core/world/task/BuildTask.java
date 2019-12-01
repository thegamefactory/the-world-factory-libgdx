package com.tgf.twf.core.world.task;

import com.google.common.collect.ImmutableList;
import com.tgf.twf.core.geo.PositionComponent;
import com.tgf.twf.core.world.AgentComponent;
import com.tgf.twf.core.world.BuildingComponent;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BuildTask implements Task {
    private final BuildingComponent buildingComponent;
    private final PositionComponent buildingPosition;

    @Override
    public List<Action> createActions(final AgentComponent agentComponent) {
        return ImmutableList.of(
                MoveActionFactory.create(agentComponent, agentComponent.getHomePosition(), buildingPosition),
                new BuildAction(buildingComponent),
                MoveActionFactory.create(agentComponent, buildingPosition, agentComponent.getHomePosition())
        );
    }
}
