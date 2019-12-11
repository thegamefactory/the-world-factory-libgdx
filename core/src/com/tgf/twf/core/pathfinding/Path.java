package com.tgf.twf.core.pathfinding;

import com.tgf.twf.core.geo.Vector2;
import lombok.RequiredArgsConstructor;

import java.io.Closeable;
import java.util.Iterator;

/**
 * A double linked list of {@link Vector2} wrapped in {@link PathNode}.
 * Caches the total length so it's not necessary to walk the list to get it.
 * Implements a forward {@link PathWalker} and a backwards {@link PathWalker} corresponding to {@link Iterator}s traversing the list head from tail
 * and tail from head, respectively.
 * Because {@link PathNode} are pooled, the {@link Path} should be closed after usage.
 */
@RequiredArgsConstructor
public class Path implements Closeable {
    private final int length;
    private final PathNode head;
    private final PathNode tail;

    /**
     * An enhanced {@link Iterator} of {@link Vector2} corresponding to the intermediate positions on the path.
     */
    public interface PathWalker extends Iterator<Vector2> {
        int getLength();
    }

    /**
     * @return a {@link PathWalker} walking the path head to tail.
     */
    public PathWalker forwardWalker() {
        return new PathWalker() {
            PathNode iterator;

            @Override
            public boolean hasNext() {
                return iterator.next != null;
            }

            @Override
            public Vector2 next() {
                if (iterator == null) {
                    iterator = head;
                } else {
                    iterator = iterator.next;
                }
                return iterator.pos;
            }

            @Override
            public int getLength() {
                return length;
            }
        };
    }

    /**
     * @return a {@link PathWalker} walking the path tail to head.
     */
    public PathWalker backwardsWalker() {
        return new PathWalker() {
            PathNode iterator = null;

            @Override
            public boolean hasNext() {
                return iterator.previous != null;
            }

            @Override
            public Vector2 next() {
                if (iterator == null) {
                    iterator = tail;
                } else {
                    iterator = iterator.previous;
                }
                return iterator.pos;
            }

            @Override
            public int getLength() {
                return length;
            }
        };
    }

    @Override
    public void close() {
        PathNode current = head;
        while (current != null) {
            final PathNode next = current.next;
            current.close();
            current = next;
        }
    }
}
