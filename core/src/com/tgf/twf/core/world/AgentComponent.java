package com.tgf.twf.core.world;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.Entity;
import com.tgf.twf.core.geo.PositionComponent;
import com.tgf.twf.core.world.task.Action;
import com.tgf.twf.core.world.task.Task;

import java.util.LinkedList;
import java.util.Queue;

public class AgentComponent extends Component {
    private final BuildingComponent home;
    private final Queue<Action> actions = new LinkedList<>();

    public AgentComponent(final Entity entity, final BuildingComponent home) {
        super(entity);
        this.home = home;
    }

    public boolean isIdle() {
        return actions.isEmpty();
    }

    public void assignTask(final Task task) {
        actions.addAll(task.createActions(this));
    }

    public Action getActiveAction() {
        return actions.peek();
    }

    public void completeAction() {
        actions.poll();
    }

    public PositionComponent getHomePosition() {
        return home.getRelatedComponent(PositionComponent.class);
    }
}
