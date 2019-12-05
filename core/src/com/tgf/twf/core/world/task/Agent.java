package com.tgf.twf.core.world.task;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.world.Building;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A component state modeling an {@link Agent} able to enqueue {@link Action}s and execute them.
 * An {@link Agent} is attached to a {@link Building} home which it goes back to (via a move action) once other actions are completed.
 */
public class Agent {
    private final Component<Building> home;
    private final Queue<Action> actions = new LinkedList<>();

    public Agent(final Component<Building> home) {
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

    public Position getHomePosition() {
        return home.getRelatedComponent(Position.class).getState();
    }
}