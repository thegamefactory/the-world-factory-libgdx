package com.tgf.twf.core.world.task;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.Entities;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.world.storage.Storage;

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
    private Queue<Task> unassignedTasks = new LinkedList<>();
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
        final Storage.MutableInventory cost = new Storage.MutableInventory();
        final Queue<Task> deadLetterQueue = new LinkedList<>();

        while (!idleAgents.isEmpty() && !unassignedTasks.isEmpty()) {
            final Agent idleAgent = idleAgents.poll();
            final Task unassignedTask = unassignedTasks.poll();
            final List<Action> actions = unassignedTask.createActions(idleAgent);
            cost.clear();
            collectCost(actions, cost);
            if (idleAgent.getHomePosition().getRelatedComponent(Storage.class).tryConsumeResources(cost)) {
                idleAgent.addActions(actions);
                busyAgents.add(idleAgent);
            } else {
                idleAgents.add(idleAgent);
                deadLetterQueue.add(unassignedTask);
            }
        }
        if (!idleAgents.isEmpty()) {
            this.unassignedTasks = deadLetterQueue;
        } else {
            this.unassignedTasks.addAll(deadLetterQueue);
        }
    }

    private static void collectCost(final List<Action> actions, final Storage.MutableInventory cost) {
        for (final Action action : actions) {
            final Storage.Inventory actionCost = action.getCost();
            cost.store(actionCost);
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
