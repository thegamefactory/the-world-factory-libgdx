package com.tgf.twf.core.pathfinding;

import com.tgf.twf.core.geo.Vector2;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Closeable;
import java.util.Iterator;

/**
 * A double linked list of {@link Vector2} wrapped in {@link PathNode}.
 * Caches the total length so it's not necessary to iterate through the list to get it.
 * Implements a forward {@link PathIterator} and a backwards {@link PathIterator} corresponding to {@link Iterator}s traversing the list head from
 * tail
 * and tail from head, respectively.
 * Because {@link PathNode} are pooled, the {@link Path} should be closed after usage.
 */
@RequiredArgsConstructor
public class Path implements Closeable {
    @Getter
    private final int length;
    private final PathNode head;
    private final PathNode tail;

    /**
     * An enhanced {@link Iterator} of {@link Vector2} corresponding to the intermediate positions on the path.
     */
    public interface PathIterator extends Iterator<Vector2>, Closeable {
        @Override
        void close();
    }

    /**
     * @return a {@link PathIterator} iterating through the path head to tail.
     */
    public PathIterator forwardIterator() {
        return new PathIterator() {
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
            public void close() {
                Path.this.close();
            }
        };
    }

    /**
     * @return a {@link PathIterator} iterating through the path tail to head.
     */
    public PathIterator backwardsIterator() {
        return new PathIterator() {
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
            public void close() {
                Path.this.close();
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
