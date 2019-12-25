package com.tgf.twf.core.pathfinding;

import com.tgf.twf.core.geo.Vector2;

/**
 * An implentation of {@link PathFinder} which finds a straight line path, regardless of any obstacles that may be present.
 */
public class StraightLinePathFinder implements PathFinder {
    public static StraightLinePathFinder INSTANCE = new StraightLinePathFinder();

    @Override
    public Path find(final Vector2 origin, final Vector2 target) {
        final int deltaX = target.x - origin.x;
        final int deltaY = target.y - origin.y;
        final int directionX = toDirection(deltaX);
        final int directionY = toDirection(deltaY);

        /*
         * This smoothens the path if the line is diagonal.
         * Assume a path that needs to move 4 on X and 1 on Y.
         * We want the move to look like:
         *
         * *--*--*
         *       |
         *       *--*--*
         *
         * We decompose the X space in equidistant segments:
         * X: 0.2, 0.4, 0.6, 0.8
         * Y: 0.5
         *
         * The changes in X and Y are then sequenced based on these floats as below
         * For every step forward we pick the lowest segment value:
         * X (0.2), X(0.4), Y(0.5), X(0.6), X(0.8)
         *
         * To avoid dealing with floats we multiply everything by (abs(deltaX) + 1) * (abs(deltaY) + 1).
         * X: 2, 4, 6, 8
         * Y: 5
         */
        final int segmentStepX = Math.abs(deltaY) + 1;
        final int segmentStepY = Math.abs(deltaX) + 1;
        int nextX = segmentStepX;
        int nextY = segmentStepY;

        int currentX = origin.x;
        int currentY = origin.y;
        final PathNode start = PathNode.from(currentX, currentY);
        PathNode current = start;

        while (current.pos.x != target.x || current.pos.y != target.y) {
            if (nextX < nextY) {
                currentX = currentX + directionX;
                nextX = nextX + segmentStepX;
            } else {
                currentY = currentY + directionY;
                nextY = nextY + segmentStepY;
            }
            current = current.setNext(currentX, currentY);
        }
        final PathNode end = current;
        final int length = Math.abs(deltaX) + Math.abs(deltaY);

        return new Path(length, start, end);
    }

    private static int toDirection(final int vector) {
        if (vector > 0) {
            return 1;
        } else if (vector < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
