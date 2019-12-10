package com.tgf.twf.core.pathfinding;

import com.tgf.twf.core.geo.Vector2;
import lombok.Data;

import java.io.Closeable;

/**
 * Internal class modeling the double linked list {@link Vector2} node of a {@link Path}.
 * {@link PathNode}s are pooled and should be closed after usage.
 */
@Data
class PathNode implements Closeable {
    public final Vector2 pos = new Vector2();
    public PathNode next = null;
    public PathNode previous = null;

    public static PathNode borrow(final int x, final int y) {
        final PathNode result = PathNodePool.INSTANCE.borrow();
        result.pos.x = x;
        result.pos.y = y;
        return result;
    }

    public PathNode setNext(final int x, final int y) {
        final PathNode next = borrow(x, y);
        next.previous = this;
        this.next = next;
        return next;
    }

    @Override
    public void close() {
        next = null;
        previous = null;
        PathNodePool.INSTANCE.release(this);
    }

    @Override
    public String toString() {
        return "(" + pos.x + ";" + pos.y + ")";
    }
}
