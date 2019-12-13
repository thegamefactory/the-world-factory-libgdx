package com.tgf.twf.core.world.task;

import com.google.common.collect.ImmutableList;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.pathfinding.MoveAction;
import com.tgf.twf.core.pathfinding.Path;
import com.tgf.twf.core.pathfinding.PathFinder;
import com.tgf.twf.core.pathfinding.StraightLinePathFinder;
import com.tgf.twf.core.util.CompletionCallback;

import java.util.function.Function;

/**
 * A factory to build a {@link Task} that chains an action to move to the action position, execute the given action, and move back the the agent home.
 */
public final class TaskFactory {
    private static final PathFinder DEFAULT_PATH_FINDER = new StraightLinePathFinder();

    public static Task create(final Function<Agent, Action> action, final Vector2 actionPosition) {
        return create(action, actionPosition, DEFAULT_PATH_FINDER);
    }

    public static Task create(final Function<Agent, Action> action, final Vector2 actionPosition, final PathFinder pathfinder) {
        return agent -> {
            final Path path = pathfinder.find(agent.getHomePosition().toVector2(), actionPosition);
            if (path == null) {
                return null;
            }

            return ImmutableList.of(
                    new MoveAction(agent, path.forwardWalker(), CompletionCallback.IDENTITY, MoveAction.MoveTarget.ACTION),
                    action.apply(agent),
                    new MoveAction(agent, path.backwardsWalker(), path::close, MoveAction.MoveTarget.HOME)
            );
        };
    }

    public static Task create(final Action action, final Vector2 actionPosition) {
        return create(agent -> action, actionPosition);
    }
}
