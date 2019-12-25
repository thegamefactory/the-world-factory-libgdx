package com.tgf.twf.core.pathfinding;

import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.agents.Agent;

import java.io.Closeable;

/**
 * Models a {@link Path.PathIterator} walked by an agent.
 */
public interface PathWalker extends Closeable {
    static PathWalker createPathWalker(final Agent agent, final Vector2 destination) {
        final Vector2 origin = agent.getRelatedComponent(Position.class).toVector2();
        if (origin.equals(destination)) {
            return NullPathWalker.INSTANCE;
        }
        final Path path = StraightLinePathFinder.INSTANCE.find(agent.getRelatedComponent(Position.class).toVector2(), destination);
        if (path == null) {
            return null;
        }
        return new PathWalkerImpl(agent, path.forwardIterator());
    }

    boolean walk();

    @Override
    void close();
}