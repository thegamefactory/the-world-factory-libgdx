package com.tgf.twf.core.world.task;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.building.Building;
import lombok.Getter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A mobile entity able to enqueue {@link Action}s and execute them.
 * An {@link Agent} is attached to a {@link Building} home which it goes back to (via a move action) once other actions are completed.
 */
public class Agent extends Component {
    @Getter
    private final Building home;
    private final Queue<Action> actions = new LinkedList<>();

    /**
     * The position of the agent, relative to the center of the tile. Purely used for rendering.
     * Each component of the vector should remain between -0.5 and 0.5, otherwise the agent will appear to be in a different tile.
     */
    @Getter
    private final Vector2f subTilePosition = new Vector2f();

    public Agent(final Building home) {
        this.home = home;
    }

    public boolean isIdle() {
        return actions.isEmpty();
    }

    public void addActions(final Collection<Action> newActions) {
        actions.addAll(newActions);
    }

    public Action getActiveAction() {
        return actions.peek();
    }

    public void completeAction() {
        actions.poll();
    }

    public Vector2 getHomePosition() {
        return home.getRelatedComponent(Position.class).toVector2();
    }
}
