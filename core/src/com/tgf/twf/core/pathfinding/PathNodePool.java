package com.tgf.twf.core.pathfinding;

import java.util.LinkedList;

/**
 * A simple pool of {@link PathNode}.
 * The pool only keeps track of available {@link PathNode}s, not busy ones, so omitting to close a {@link PathNode} doesn't have serious consequences.
 */
public class PathNodePool {
    private final LinkedList<PathNode> available = new LinkedList<>();

    public PathNode borrow() {
        if (available.isEmpty()) {
            return new PathNode();
        } else {
            return available.poll();
        }
    }

    public void release(final PathNode pathNode) {
        available.push(pathNode);
    }

    public static PathNodePool INSTANCE = new PathNodePool();
}
