package com.tgf.twf.core.world.task;

import com.google.common.collect.ImmutableList;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.pathfinding.MoveAction;
import com.tgf.twf.core.pathfinding.Path;
import com.tgf.twf.core.pathfinding.Pathfinder;
import com.tgf.twf.core.pathfinding.StraightLinePathFinder;
import com.tgf.twf.core.util.CompletionCallback;

import java.util.List;

/**
 * A factory to build a {@link Task} that chains an action to move to the action position, execute the given action, and move back the the agent home.
 */
public final class TaskFactory {
    private static final Pathfinder DEFAULT_PATHFINDER = new StraightLinePathFinder();

    public static Task create(final Action action, final Vector2 actionPosition) {
        return create(action, actionPosition, DEFAULT_PATHFINDER);
    }

    public static Task create(final Action action, final Vector2 actionPosition, final Pathfinder pathfinder) {
        return new Task() {
            @Override
            public List<Action> createActions(final Agent agent) {
                final Path path = pathfinder.find(agent.getHomePosition().toVector2(), actionPosition);
                if (path == null) {
                    return null;
                }

                return ImmutableList.of(
                        new MoveAction(agent, path.forwardWalker(), CompletionCallback.IDENTITY),
                        action,
                        new MoveAction(agent, path.backwardsWalker(), path::close)
                );
            }

            @Override
            public String getName() {
                return action.getName();
            }
        };
    }
}
