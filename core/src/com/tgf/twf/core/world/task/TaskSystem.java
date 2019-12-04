package com.tgf.twf.core.world.task;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.ComponentLifecycleListener;
import com.tgf.twf.core.ecs.System;

import java.time.Duration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * The {@link TaskSystem} is the brain ticking the {@link Task}s.
 * It maintains a list of idle {@link Agent}s and assign them incoming {@link Task}s by building and queuing the list of corresponding task
 * {@link Action}s to the {@link Agent}.
 * It checks the state of busy {@link Agent}s and ticks their active {@link Action}. When the active {@link Action} is complete, it dequeues the
 * {@link Action} queue of the {@link Agent}; if the queue is empty, it puts back the {@link Agent} into the idle {@link Agent} pool.
 */
public class TaskSystem implements System, ComponentLifecycleListener<Agent> {
    private final Queue<Task> unassignedTasks = new LinkedList<>();
    private final Queue<Component<Agent>> idleAgents = new LinkedList<>();
    private final List<Component<Agent>> busyAgents = new LinkedList<>();

    public void addTask(final Task task) {
        unassignedTasks.add(task);
    }

    @Override
    public void onComponentAttached(final Component<Agent> agentStateComponent) {
        final Agent agent = agentStateComponent.getState();
        if (agent.isIdle()) {
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
            final Component<Agent> idleAgentComponent = idleAgents.poll();
            final Task unassignedTask = unassignedTasks.poll();
            final List<Action> actions = unassignedTask.createActions(idleAgentComponent);
            idleAgentComponent.getState().addActions(actions);
            busyAgents.add(idleAgentComponent);
        }
    }

    public void executeTasks(final Duration delta) {
        final Set<Component<Agent>> idlingAgents = new HashSet<>();
        busyAgents.forEach(
                agentComponent -> {
                    final Agent agent = agentComponent.getState();
                    final Action activeAction = agent.getActiveAction();
                    activeAction.update(delta);
                    if (activeAction.isComplete()) {
                        agent.completeAction();
                    }
                    if (agent.isIdle()) {
                        idlingAgents.add(agentComponent);
                    }
                }
        );
        busyAgents.removeIf(idlingAgents::contains);
        idleAgents.addAll(idlingAgents);
    }
}
