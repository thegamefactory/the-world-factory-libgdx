package com.tgf.twf.core.world.agents;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.Entities;
import com.tgf.twf.core.geo.GeoMap;

import java.util.LinkedList;

/**
 * A system maintaining game actions and ticking the agents.
 * Will evolve to contain optimized logic to dispatch actions to agents.
 */
public class TaskSystem {
    final LinkedList<Agent> agents = new LinkedList<>();
    // TODO: storing the agents here is duplicated compared to storing them in entities
    final LinkedList<Action> actions = new LinkedList<>();

    private final AgentStateTickContext agentStateTickContext;

    public TaskSystem(final GeoMap geoMap) {
        agentStateTickContext = new AgentStateTickContext(this, geoMap);
        Entities.registerComponentEventListener(this::handle, Agent.class, Component.CreationEvent.class);
    }

    public void handle(final Agent sender, final Component.CreationEvent event) {
        agents.add(sender);
    }

    public void tick() {
        for (final Agent agent : agents) {
            final AgentState nextState = agent.getState().tick(agent, agentStateTickContext);
            if (null != nextState) {
                agent.setState(nextState);
            }
        }
    }

    public Action removeFirstAction() {
        if (actions.isEmpty()) {
            return null;
        }
        return actions.removeFirst();
    }

    public void addActionLast(final Action action) {
        actions.addLast(action);
    }
}
