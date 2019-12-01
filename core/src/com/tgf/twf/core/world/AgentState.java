package com.tgf.twf.core.world;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.world.task.Action;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class AgentState {
    private final Component<BuildingState> home;
    private final Queue<Action> actions = new LinkedList<>();

    public AgentState(final Component<BuildingState> home) {
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
