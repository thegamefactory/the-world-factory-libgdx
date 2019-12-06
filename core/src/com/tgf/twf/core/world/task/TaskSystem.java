package com.tgf.twf.core.world.task;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.Entities;
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
public class TaskSystem implements System {
    private final Queue<Task> unassignedTasks = new LinkedList<>();
    private final Queue<Agent> idleAgents = new LinkedList<>();
    private final List<Agent> busyAgents = new LinkedList<>();

    public TaskSystem() {
        Entities.registerComponentEventListener(this::handle, Agent.class, Component.CreationEvent.class);
    }

    public void addTask(final Task task) {
        unassignedTasks.add(task);
    }

    public void handle(final Agent sender, final Component.CreationEvent event) {
        if (sender.isIdle()) {
            idleAgents.add(sender);
        } else {
            busyAgents.add(sender);
        }
    }

    @Override
    public void update(final Duration delta) {
        assignTaskToIdleAgents();
        executeTasks(delta);
    }

    private void assignTaskToIdleAgents() {
        while (!idleAgents.isEmpty() && !unassignedTasks.isEmpty()) {
            final Agent idleAgent = idleAgents.poll();
            final Task unassignedTask = unassignedTasks.poll();
            final List<Action> actions = unassignedTask.createActions(idleAgent);
            idleAgent.addActions(actions);
            busyAgents.add(idleAgent);
        }
    }

    public void executeTasks(final Duration delta) {
        final Set<Agent> idlingAgents = new HashSet<>();
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
