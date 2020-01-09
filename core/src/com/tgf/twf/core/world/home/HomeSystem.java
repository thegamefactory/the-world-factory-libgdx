package com.tgf.twf.core.world.home;

import com.tgf.twf.core.ecs.Entities;
import com.tgf.twf.core.ecs.Entity;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.world.agents.Agent;
import com.tgf.twf.core.world.agents.StarvingAgentState;
import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.core.world.storage.AnyResourceTypeFixedCapacity;
import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.storage.Storage;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * The system that manages {@link Home}s and population variations.
 */
public class HomeSystem implements System {
    final LinkedList<Agent> starvingAgents = new LinkedList<>();

    public HomeSystem() {
        Entities.registerComponentEventListener(this::handle, Agent.class, StarvingAgentState.StarvingAgentEvent.class);
    }

    @Override
    public void tick() {
        removeStarvingAgents();

        final Collection<Home> spawningHomes = Entities.allComponents(Home.class).filter(this::isHomeSpawning).collect(Collectors.toList());

        for (final Home spawningHome : spawningHomes) {
            spawningHome.getRelatedComponent(Storage.class).retrieveToEmpty(ResourceType.FOOD, Rules.AGENT_MAX_FOOD + Rules.AGENT_FOOD_COST);
            createAgent(spawningHome);
        }
    }

    private boolean isHomeSpawning(final Home home) {
        if (home.getAgentCount() >= home.getAgentCapacity()) {
            return false;
        }

        final Storage homeStorage = home.getRelatedComponent(Storage.class);
        if (homeStorage == null) {
            return false;
        }

        return homeStorage.getStored(ResourceType.FOOD) >= Rules.AGENT_MAX_FOOD + Rules.AGENT_FOOD_COST;
    }

    public void createAgent(final Home home) {
        final Agent agent = new Agent(home, home.getPosition());

        Entity.builder()
                .withComponent(agent)
                .withComponent(new Storage(new AnyResourceTypeFixedCapacity(Rules.AGENT_STORAGE_CAPACITY)))
                .buildAndAttach();

        home.attachAgent(agent);
    }

    private void handle(final Agent sender, final StarvingAgentState.StarvingAgentEvent event) {
        starvingAgents.add(sender);
    }

    private void removeStarvingAgents() {
        for (final Agent agent : starvingAgents) {
            assert agent.getAction() == null;

            if (agent.isHome()) {
                agent.getHomeStorage().storeToCapacity(ResourceType.FOOD, Rules.AGENT_FOOD_COST);
            }

            agent.getEntity().detach();
        }
        starvingAgents.clear();
    }
}
