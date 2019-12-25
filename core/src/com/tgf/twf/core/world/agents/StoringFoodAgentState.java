package com.tgf.twf.core.world.agents;

import com.tgf.twf.core.world.building.Building;
import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.storage.Storage;

/**
 * A {@link AgentState} in which the {@link Agent} stores food to the storage located at its position.
 */
public class StoringFoodAgentState implements AgentState {
    private StoringFoodAgentState() {

    }

    public static StoringFoodAgentState INSTANCE = new StoringFoodAgentState();

    @Override
    public AgentState tick(final Agent agent, final AgentStateTickContext agentStateTickContext) {
        if (agent.getFood() <= Rules.AGENT_IDEAL_FOOD_LEVEL) {
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

        final int quantity = agent.retrieve(ResourceType.FOOD, 1);
        final int stored = storage.storeToCapacity(ResourceType.FOOD, quantity);
        if (stored != quantity) {
            agent.store(ResourceType.FOOD, quantity - stored);
            return IdleAgentState.INSTANCE;
        }

        return null;
    }
}
