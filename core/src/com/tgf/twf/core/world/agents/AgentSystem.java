package com.tgf.twf.core.world.agents;

import com.tgf.twf.core.ecs.Entities;
import com.tgf.twf.core.geo.GeoMap;
import com.tgf.twf.core.world.daytimesystem.DaytimeSystem;

import java.util.LinkedList;

/**
 * A system maintaining game actions and ticking the agents.
 * Will evolve to contain optimized logic to dispatch actions to agents.
 */
public class AgentSystem {


    final LinkedList<Action> actions = new LinkedList<>();

    private final AgentStateTickContext agentStateTickContext;

    public AgentSystem(final DaytimeSystem daytimeSystem, final GeoMap geoMap) {

        agentStateTickContext = new AgentStateTickContext(this, daytimeSystem, geoMap);
    }

    public void tick() {
        Entities.allComponents(Agent.class).forEach(agent -> {
            final AgentState nextState = agent.getState().tick(agent, agentStateTickContext);
            if (null != nextState) {
                agent.setState(nextState);
            }
        });
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
