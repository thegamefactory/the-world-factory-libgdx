package com.tgf.twf.core.world.agents;

import com.tgf.twf.core.geo.Vector2;

/**
 * Something that needs to be executed by an agent.
 * Can be player triggered (ex: a building construction action) or game system triggered (eg: a field harvesting action once a field has grown).
 */
public interface Action {
    Vector2 getPosition();

    /**
     * Executes the action for one tick with the given {@link Agent}.
     *
     * @return true if the action is completed.
     */
    boolean tick(final Agent agent);
}
