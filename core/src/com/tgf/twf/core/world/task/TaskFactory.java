package com.tgf.twf.core.world.task;

import com.google.common.collect.ImmutableList;
import com.tgf.twf.core.geo.Vector2;

import java.util.List;

/**
 * A factory to build a {@link Task} that chains an action to move to the action position, execute the given action, and move back the the agent home.
 */
public final class TaskFactory {
    public static Task create(final Action action, final Vector2 actionPosition) {
        return new Task() {
            @Override
            public List<Action> createActions(final Agent agent) {
                final Vector2 agentHomePosition = agent.getHomePosition().toVector2();
                return ImmutableList.of(
                        MoveActionFactory.create(agent, agentHomePosition, actionPosition, MoveActionFactory.MoveType.STANDARD),
                        action,
                        MoveActionFactory.create(agent, actionPosition, agentHomePosition, MoveActionFactory.MoveType.HOME)
                );
            }

            @Override
            public String getName() {
                return action.getName();
            }
        };
    }
}
