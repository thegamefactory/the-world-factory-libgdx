package com.tgf.twf.core.pathfinding;

import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.util.CompletionCallback;
import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.core.world.storage.Inventory;
import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.task.Action;
import com.tgf.twf.core.world.task.Agent;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

/**
 * An {@link Action} that models the progressive movement of an {@link Agent} through a {@link Path.PathWalker}.
 */
@RequiredArgsConstructor
public class MoveAction implements Action {
    private final Agent agent;

    private Path.PathWalker pathWalker;
    private CompletionCallback completionCallback;

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

    public enum MoveTarget {
        ACTION,
        HOME
    }

    private Event nextEvent;
    private int elapsedNanosSinceLastStateChange = 0;
    private boolean isComplete = false;
    private Vector2 currentPosition;
    private Vector2 nextPosition;
    private final Vector2 currentDirection = new Vector2();
    private final MoveTarget moveTarget;

    public MoveAction(final Agent agent,
                      final Path.PathWalker pathWalker,
                      final CompletionCallback completionCallback,
                      final MoveTarget moveTarget) {
        this.agent = agent;
        this.pathWalker = pathWalker;
        this.completionCallback = completionCallback;
        this.moveTarget = moveTarget;

        this.elapsedNanosSinceLastStateChange = 0;
        this.nextEvent = Event.CROSS_TILE_BORDER;
        this.nextPosition = pathWalker.next();
        updateDirection();
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }

    @Override
    public void update(final Duration delta) {
        if (isComplete) {
            return;
        }

        elapsedNanosSinceLastStateChange = elapsedNanosSinceLastStateChange + delta.getNano();

        while (elapsedNanosSinceLastStateChange > halfTileInNanos()) {
            elapsedNanosSinceLastStateChange = elapsedNanosSinceLastStateChange - halfTileInNanos();
            if (Event.CROSS_TILE_BORDER == nextEvent) {
                agent.getRelatedComponent(Position.class).setPosition(nextPosition);
                nextEvent = Event.UPDATE_DIRECTION;
            } else {
                updateDirection();
                if (nextPosition == null) {
                    isComplete = true;
                    elapsedNanosSinceLastStateChange = 0;
                    completionCallback.complete();
                }
                nextEvent = Event.CROSS_TILE_BORDER;
            }
        }

        final float tileLerpFactor = tileLerpFactor(elapsedNanosSinceLastStateChange, nextEvent);
        agent.getSubTilePosition().x = currentDirection.x * tileLerpFactor;
        agent.getSubTilePosition().y = currentDirection.y * tileLerpFactor;
    }

    @Override
    public Inventory getCost() {
        if (moveTarget == MoveTarget.ACTION) {
            return Action.Cost.of(ResourceType.FOOD, Math.max(pathWalker.getLength() - 1, 0));
        }
        return Cost.FREE;
    }

    @Override
    public String getName() {
        return "move";
    }

    private void updateDirection() {
        currentPosition = nextPosition;
        if (pathWalker.hasNext()) {
            nextPosition = pathWalker.next();
            Vector2.minus(nextPosition, currentPosition, currentDirection);
        } else {
            nextPosition = null;
            currentDirection.x = 0;
            currentDirection.y = 0;
        }
    }

    private static int halfTileInNanos() {
        return Rules.AGENT_NANOS_PER_TILE / 2;
    }

    private static int fullTileInNanos() {
        return Rules.AGENT_NANOS_PER_TILE;
    }

    private static float tileLerpFactor(final int ellapsedNanosSinceLastTile, final Event nextEvent) {
        final float progress = (float) ellapsedNanosSinceLastTile / fullTileInNanos();
        if (Event.CROSS_TILE_BORDER == nextEvent) {
            return progress;
        } else {
            return progress - 0.5f;
        }
    }
}
