package com.tgf.twf.core.world.task;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.geo.Position;

import java.time.Duration;

/**
 * A factory for a {@link TimedAction} representing a move action.
 * Because the current implementation of move is just wait some time and then update the position of the agent to the destination, there's no need
 * for a dedicated MoveAction class yet.
 */
public final class MoveActionFactory {
    private static final double SPEED = 1.0;

    public static Action create(
            final Component<Agent> agent,
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
