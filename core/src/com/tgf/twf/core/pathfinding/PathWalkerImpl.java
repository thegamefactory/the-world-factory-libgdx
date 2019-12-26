package com.tgf.twf.core.pathfinding;

import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.agents.Agent;
import com.tgf.twf.core.world.rules.Rules;
import lombok.RequiredArgsConstructor;

/**
 * Main implementation of {@link PathWalker}.
 * Models a {@link Path.PathIterator} walked by an agent.
 */
@RequiredArgsConstructor
public class PathWalkerImpl implements PathWalker {
    final Agent agent;

    private Path.PathIterator pathIterator;
    /*
     * The diagram below illustrates how a MoveAction progresses.
     * ellapsedNanosSinceLastStateChange is increased at each update call.
     * State transitions are triggered when ellapsedNanosSinceLastStateChange breaches a defined threshold.
     *
     * Each number in square brackets corresponds to an event, except the initial state.
     * Each vector in round brackets corresponds to a tile position.
     *
     * 0. Initial state.
     *    nextEvent is set to CROSS_TILE_BORDER
     *    currentTile is set to (0,0), nextPosition is set to (1,0), currentDirection is set to (1,0)
     *
     * 1. Cross tile border event
     *    nextEvent is set to UPDATE_DIRECTION
     *    the agent position is set to nextPosition (1,0)
     *
     * 2. Update direction event
     *    nextEvent is set to CROSS_TILE_BORDER
     *    currentTile is set to nextPosition (1,0), nextPosition is set to (1,1), currentDirection is set to (0,1)
     *
     * 3. Cross tile border event
     *    nextEvent is set to UPDATE_DIRECTION
     *    the agent position is set to nextPosition (1,1)
     *
     * 4. Update direction
     *    because there's no nextPosition, the isComplete flag is set to true
     *
     *           *---------*
     *           |  (1,1)  |
     *           |   [4]   |
     *           |    ^    |
     * *---------*---[3]---*
     * |         |    ^    |
     * |   [0]->[1]->[2]   |
     * |  (0,0)  |  (1,0)  |
     * *---------*---------*
     *
     * At each update, the agent subTilePosition is updated with a vector within the rectangle (-0.5, -0.5),(0.5, 0.5), corresponding to the
     * relative position of the agent compared to the center of the tile.
     */

    private enum Event {
        CROSS_TILE_BORDER,
        UPDATE_DIRECTION
    }

    private Event nextEvent;
    private int tickCount = 0;
    private Vector2 currentPosition;
    private Vector2 nextPosition;
    private final Vector2 currentDirection = new Vector2();

    public PathWalkerImpl(final Agent agent,
                          final Path.PathIterator pathIterator) {
        this.agent = agent;
        this.pathIterator = pathIterator;

        this.tickCount = 0;
        this.nextEvent = Event.CROSS_TILE_BORDER;
        this.nextPosition = pathIterator.next();
        updateDirection();
    }

    @Override
    public boolean walk() {
        tickCount++;

        if (tickCount == Rules.AGENT_TICKS_PER_TILE / 2) {
            tickCount = 0;
            if (Event.CROSS_TILE_BORDER == nextEvent) {
                agent.setPosition(nextPosition);
                nextEvent = Event.UPDATE_DIRECTION;
            } else {
                updateDirection();
                if (nextPosition == null) {
                    agent.getSubTilePosition().x = 0;
                    agent.getSubTilePosition().y = 0;
                    return true;
                }
                nextEvent = Event.CROSS_TILE_BORDER;
            }
        }

        final float tileLerpFactor = tileLerpFactor(nextEvent);
        agent.getSubTilePosition().x = currentDirection.x * tileLerpFactor;
        agent.getSubTilePosition().y = currentDirection.y * tileLerpFactor;

        return false;
    }

    @Override
    public void close() {
        pathIterator.close();
    }

    private void updateDirection() {
        currentPosition = nextPosition;
        if (pathIterator.hasNext()) {
            nextPosition = pathIterator.next();
            Vector2.minus(nextPosition, currentPosition, currentDirection);
        } else {
            nextPosition = null;
            currentDirection.x = 0;
            currentDirection.y = 0;
        }
    }

    private float tileLerpFactor(final Event nextEvent) {
        final float progress = (float) tickCount / Rules.AGENT_TICKS_PER_TILE;
        if (Event.CROSS_TILE_BORDER == nextEvent) {
            return progress;
        } else {
            return progress - 0.5f;
        }
    }
}
