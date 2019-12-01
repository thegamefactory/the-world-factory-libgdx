package com.tgf.twf.core.world.task;

import com.tgf.twf.core.geo.PositionComponent;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.AgentComponent;

import java.time.Duration;

public final class MoveActionFactory {
    private static final double SPEED = 1.0;

    public static TimedAction create(
            final AgentComponent agent,
            final PositionComponent startPositionComponent,
            final PositionComponent targetPositionComponent) {
        final Vector2 targetPosition = targetPositionComponent.getPosition();
        return new TimedAction(
                Duration.ofMillis((long) (1000 * targetPositionComponent.manatthanDistance(startPositionComponent) / SPEED)),
                () -> {
                    final PositionComponent positionComponent = agent.getRelatedComponent(PositionComponent.class);
                    positionComponent.setPosition(targetPosition);
                }
        );
    }
}
