package com.tgf.twf.core.world.task;

import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.geo.Vector2;

import java.time.Duration;

/**
 * A factory for a {@link TimedAction} representing a move action.
 * Because the current implementation of move is just wait some time and then update the position of the agent to the destination, there's no need
 * for a dedicated MoveAction class yet.
 */
public final class MoveActionFactory {
    private static final double SPEED = 1.0;

    public static Action create(
            final Agent agent,
            final Vector2 startPosition,
            final Vector2 targetPosition) {
        return new TimedAction(
                Duration.ofMillis((long) (1000 * targetPosition.manatthanDistance(startPosition) / SPEED)),
                () -> {
                    final Position position = agent.getRelatedComponent(Position.class);
                    position.setPosition(targetPosition);
                }
        );
    }
}
