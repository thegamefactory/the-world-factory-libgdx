package com.tgf.twf.core.world.task;

import java.util.List;

/**
 * A {@link Task} represents a sequence of actions that need to be undertaken by an {@link Agent}, either because the player has created the
 * {@link Task} directly, or because some game conditions have been met.
 *
 * Newly created tasks will be pushed to the unassigned tasks of the {@link TaskSystem} and assigned to idle {@link Agent}s.
 */
public interface Task {
    List<Action> createActions(final Agent agent);
}
