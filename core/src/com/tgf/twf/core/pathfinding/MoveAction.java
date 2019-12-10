package com.tgf.twf.core.pathfinding;

import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.util.CompletionCallback;
import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.storage.Storage;
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

    private int ellapsedNanosSinceLastTile = 0;
    private boolean isComplete = false;

    public MoveAction(final Agent agent,
                      final Path.PathWalker pathWalker,
                      final CompletionCallback completionCallback) {
        this.agent = agent;
        this.pathWalker = pathWalker;
        this.completionCallback = completionCallback;
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

        ellapsedNanosSinceLastTile = ellapsedNanosSinceLastTile + delta.getNano();

        while (ellapsedNanosSinceLastTile > Rules.AGENT_NANOS_PER_TILE && !isComplete) {
            ellapsedNanosSinceLastTile = ellapsedNanosSinceLastTile - Rules.AGENT_NANOS_PER_TILE;
            agent.getRelatedComponent(Position.class).setPosition(pathWalker.next());
            if (!pathWalker.hasNext()) {
                isComplete = true;
                ellapsedNanosSinceLastTile = 0;
                completionCallback.complete();
            }
        }
    }

    @Override
    public Storage.Inventory getCost() {
        return Action.Cost.of(ResourceType.FOOD, Math.max(pathWalker.getLength() - 1, 0));
    }

    @Override
    public String getName() {
        return "move";
    }
}
