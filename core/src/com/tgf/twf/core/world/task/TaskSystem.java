package com.tgf.twf.core.world.task;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.Entities;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.storage.HashMapInventory;
import com.tgf.twf.core.world.storage.Inventory;
import com.tgf.twf.core.world.storage.MutableInventory;
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

    public void addTask(final Action action, final Vector2 position) {
        unassignedTasks.add(TaskFactory.create(action, position));
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
        final Queue<Task> deadLetterQueue = new LinkedList<>();

        while (!idleAgents.isEmpty() && !unassignedTasks.isEmpty()) {
            final Agent idleAgent = idleAgents.poll();
            final Task unassignedTask = unassignedTasks.poll();
            final List<Action> actions = unassignedTask.createActions(idleAgent);
            final Storage homeStorage = idleAgent.getHome().getRelatedComponent(Storage.class);

            boolean success = false;
            final MutableInventory cost = computeCost(actions);
            if (canCostBePaid(homeStorage, cost)) {
                final MutableInventory production = computeProduction(actions);
                if (canProductionBeStored(homeStorage, production)) {
                    homeStorage.retrieve(cost);
                    homeStorage.reserve(production);
                    idleAgent.addActions(actions);
                    busyAgents.add(idleAgent);
                    success = true;
                }
            }

            if (!success) {
                idleAgents.add(idleAgent);
                deadLetterQueue.add(unassignedTask);
            }
        }
        if (unassignedTasks.isEmpty()) {
            this.unassignedTasks = deadLetterQueue;
        } else {
            this.unassignedTasks.addAll(deadLetterQueue);
        }
    }

    private static boolean canCostBePaid(final Storage homeStorage, final MutableInventory cost) {
        return homeStorage.canRetrieve(cost);
    }

    private static MutableInventory computeCost(final List<Action> actions) {
        final MutableInventory cost = new HashMapInventory();
        for (final Action action : actions) {
            final Inventory actionCost = action.getCost();
            cost.store(actionCost);
        }
        return cost;
    }

    private static boolean canProductionBeStored(final Storage homeStorage, final MutableInventory production) {
        return homeStorage.canReserve(production);
    }

    private static MutableInventory computeProduction(final List<Action> actions) {
        final MutableInventory production = new HashMapInventory();
        for (final Action action : actions) {
            final Inventory actionProduction = action.getProduction();
            production.store(actionProduction);
        }
        return production;
    }

    public void executeTasks(final Duration delta) {
        final Set<Agent> idlingAgents = new HashSet<>();
        busyAgents.forEach(
                agent -> {
                    final Action activeAction = agent.getActiveAction();
                    activeAction.update(delta);
                    if (activeAction.isComplete()) {
                        agent.completeAction();
                        agent.getRelatedComponent(Storage.class).store(activeAction.getProduction());
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
