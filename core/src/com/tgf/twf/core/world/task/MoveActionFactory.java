package com.tgf.twf.core.world.task;

import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.storage.ResourceType;

import java.time.Duration;

/**
 * A factory for a {@link TimedAction} representing a move action.
 * Because the current implementation of move is just wait some time and then update the position of the agent to the destination, there's no need
 * for a dedicated MoveAction class yet.
 */
public final class MoveActionFactory {
    private static final double SPEED = 1.0;

    public enum MoveType {
        STANDARD,
        HOME
    }

    public static Action create(
            final Agent agent,
            final Vector2 startPosition,
            final Vector2 targetPosition,
            final MoveType moveType) {
        return TimedAction.builder()
                .name("move")
                .duration(Duration.ofMillis((long) (1000 * targetPosition.manatthanDistance(startPosition) / SPEED)))
                .completionCallback(() -> {
                    final Position position = agent.getRelatedComponent(Position.class);
                    position.setPosition(targetPosition);
                })
                .cost(moveType.equals(MoveType.HOME) ?
                        Action.Cost.FREE :
                        Action.Cost.of(ResourceType.FOOD, foodCostForPath(startPosition, targetPosition)))
                .build();
    }

    private static int foodCostForPath(final Vector2 startPosition, final Vector2 targetPosition) {
        final int distance = startPosition.manatthanDistance(targetPosition);
        return Math.max(distance - 1, 0);
    }
}
