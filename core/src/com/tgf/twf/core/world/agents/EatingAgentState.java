package com.tgf.twf.core.world.agents;

import com.tgf.twf.core.world.building.Building;
import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.storage.Storage;

/**
 * A {@link AgentState} in which the {@link Agent} consumes its food and gets energy for that.
 */
public class EatingAgentState implements AgentState {
    private EatingAgentState() {

    }

    public static EatingAgentState INSTANCE = new EatingAgentState();

    @Override
    public AgentState tick(final Agent agent, final AgentStateTickContext agentStateTickContext) {
        if (agent.getEatenFood() >= Rules.AGENT_FOOD_CONSUMPTION_PER_DAY) {
            agent.resetEatenFood();
            return SleepingAgentState.INSTANCE;
        }

        final Building building = agentStateTickContext.getGeoMap().getBuildingAt(agent.getPosition());
        if (building == null) {
            // TODO: remove agent
            return null;
        }
        final Storage storage = building.getRelatedComponent(Storage.class);
        if (storage == null) {
            // TODO: remove agent
            return null;
        }

        final int retrieved = storage.retrieveToEmpty(ResourceType.FOOD, 1);
        if (retrieved == 0) {
            // TODO: handle deadlock
            return null;
        }

        agent.eat(retrieved);
        return null;
    }
}
