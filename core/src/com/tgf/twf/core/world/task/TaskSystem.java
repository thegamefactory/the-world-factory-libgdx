package com.tgf.twf.core.world.task;

import com.tgf.twf.core.ecs.ComponentLifecycleListener;
import com.tgf.twf.core.ecs.Entity;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.world.AgentComponent;

import java.time.Duration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class TaskSystem implements System, ComponentLifecycleListener<AgentComponent> {
    private final Queue<Task> unassignedTasks = new LinkedList<>();
    private final Queue<AgentComponent> idleAgents = new LinkedList<>();
    private final List<AgentComponent> busyAgents = new LinkedList<>();

    public void addTask(final Task task) {
        unassignedTasks.add(task);
    }

    @Override
    public void onComponentAttached(final Entity entity, final AgentComponent agent) {
        if (agent.isIdle()) {
            idleAgents.add(agent);
        } else {
            busyAgents.add(agent);
        }
    }

    @Override
    public void onComponentDetached(final Entity entity, final AgentComponent component) {
        throw new UnsupportedOperationException("Cannot remove agents");
    }

    @Override
    public void update(final Duration delta) {
        assignTaskToIdleAgents();
        executeTasks(delta);
    }

    private void assignTaskToIdleAgents() {
        while (!idleAgents.isEmpty() && !unassignedTasks.isEmpty()) {
            final AgentComponent idleAgent = idleAgents.poll();
            final Task unassignedTask = unassignedTasks.poll();
            idleAgent.assignTask(unassignedTask);
            busyAgents.add(idleAgent);
        }
    }

    public void executeTasks(final Duration delta) {
        final Set<AgentComponent> idlingAgents = new HashSet<>();
        busyAgents.forEach(
                agent -> {
                    final Action activeAction = agent.getActiveAction();
                    activeAction.update(delta);
                    if (activeAction.isComplete()) {
                        agent.completeAction();
                    }
                    if (agent.isIdle()) {
                        idlingAgents.add(agent);
                    }
                }
        );
        busyAgents.removeIf(idlingAgents::contains);
        idleAgents.addAll(idlingAgents);
    }
}
