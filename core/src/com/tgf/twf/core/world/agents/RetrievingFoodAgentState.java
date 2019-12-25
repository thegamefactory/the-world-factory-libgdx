package com.tgf.twf.core.world.agents;

import com.tgf.twf.core.world.building.Building;
import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.storage.Storage;

/**
 * A {@link AgentState} in which the {@link Agent} retrieves food from the storage located at its position.
 */
public class RetrievingFoodAgentState implements AgentState {
    private RetrievingFoodAgentState() {

    }

    public static RetrievingFoodAgentState INSTANCE = new RetrievingFoodAgentState();

    @Override
    public AgentState tick(final Agent agent, final AgentStateTickContext agentStateTickContext) {
        if (agent.getFood() >= Rules.AGENT_IDEAL_FOOD_LEVEL) {
            return IdleAgentState.INSTANCE;
        }

        final Building building = agentStateTickContext.getGeoMap().getBuildingAt(agent.getPosition());
        if (building == null) {
            return IdleAgentState.INSTANCE;
        }
        final Storage storage = building.getRelatedComponent(Storage.class);
        if (storage == null) {
            return IdleAgentState.INSTANCE;
        }

        final int retrieved = storage.retrieveToEmpty(ResourceType.FOOD, 1);
        if (retrieved == 0) {
            return IdleAgentState.INSTANCE;
        }

        final int stored = agent.store(ResourceType.FOOD, retrieved);
        if (stored != retrieved) {
            storage.storeToCapacity(ResourceType.FOOD, retrieved - stored);
            return IdleAgentState.INSTANCE;
        }

        return null;
    }
}
