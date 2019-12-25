package com.tgf.twf.core.pathfinding;

/**
 * Models an empty {@link Path.PathIterator} walked by an agent, which is immediately completed.
 */
public class NullPathWalker implements PathWalker {
    public static NullPathWalker INSTANCE = new NullPathWalker();

    private NullPathWalker() {

    }

    @Override
    public boolean walk() {
        return true;
    }

    @Override
    public void close() {

    }
}
