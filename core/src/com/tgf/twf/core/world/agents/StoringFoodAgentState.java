package com.tgf.twf.core.world.agents;

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
        final Storage storage = agentStateTickContext.getGeoMap().getStorageAt(agent.getPosition());
        if (storage == null) {
            return IdleAgentState.INSTANCE;
        }

        final int quantity = agent.retrieve(ResourceType.FOOD, 1);
        if (quantity == 0) {
            return IdleAgentState.INSTANCE;
        }

        final int stored = storage.storeToCapacity(ResourceType.FOOD, quantity);
        if (stored != quantity) {
            agent.store(ResourceType.FOOD, quantity - stored);
            return IdleAgentState.INSTANCE;
        }

        return null;
    }
}
