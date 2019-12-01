package com.tgf.twf.core.world.task;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.world.AgentState;

import java.time.Duration;

public final class MoveActionFactory {
    private static final double SPEED = 1.0;

    public static TimedAction create(
            final Component<AgentState> agent,
            final Position startPosition,
            final Position targetPosition) {
        return new TimedAction(
                Duration.ofMillis((long) (1000 * targetPosition.manatthanDistance(startPosition) / SPEED)),
                () -> {
                    final Component<Position> positionComponent = agent.getRelatedComponent(Position.class);
                    positionComponent.updateState(targetPosition);
                }
        );
    }
}
