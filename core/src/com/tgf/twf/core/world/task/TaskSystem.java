package com.tgf.twf.core.world.task;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.ComponentLifecycleListener;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.world.AgentState;

import java.time.Duration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class TaskSystem implements System, ComponentLifecycleListener<AgentState> {
    private final Queue<Task> unassignedTasks = new LinkedList<>();
    private final Queue<Component<AgentState>> idleAgents = new LinkedList<>();
    private final List<Component<AgentState>> busyAgents = new LinkedList<>();

    public void addTask(final Task task) {
        unassignedTasks.add(task);
    }

    @Override
    public void onComponentAttached(final Component<AgentState> agentStateComponent) {
        final AgentState agentState = agentStateComponent.getState();
        if (agentState.isIdle()) {
            idleAgents.add(agentStateComponent);
        } else {
            busyAgents.add(agentStateComponent);
        }
    }

    @Override
    public void update(final Duration delta) {
        assignTaskToIdleAgents();
        executeTasks(delta);
    }

    private void assignTaskToIdleAgents() {
        while (!idleAgents.isEmpty() && !unassignedTasks.isEmpty()) {
            final Component<AgentState> idleAgentComponent = idleAgents.poll();
            final Task unassignedTask = unassignedTasks.poll();
            final List<Action> actions = unassignedTask.createActions(idleAgentComponent);
            idleAgentComponent.getState().addActions(actions);
            busyAgents.add(idleAgentComponent);
        }
    }

    public void executeTasks(final Duration delta) {
        final Set<Component<AgentState>> idlingAgents = new HashSet<>();
        busyAgents.forEach(
                agentStateComponent -> {
                    final AgentState agentState = agentStateComponent.getState();
                    final Action activeAction = agentState.getActiveAction();
                    activeAction.update(delta);
                    if (activeAction.isComplete()) {
                        agentState.completeAction();
                    }
                    if (agentState.isIdle()) {
                        idlingAgents.add(agentStateComponent);
                    }
                }
        );
        busyAgents.removeIf(idlingAgents::contains);
        idleAgents.addAll(idlingAgents);
    }
}
