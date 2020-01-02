package com.tgf.twf.core.world.agents;

import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.storage.Storage;

/**
 * A {@link AgentState} in which the {@link Agent} consumes food from the storage at the current location.
 */
public class EatingAgentState implements AgentState {
    private EatingAgentState() {

    }

    public static EatingAgentState INSTANCE = new EatingAgentState();

    @Override
    public AgentState tick(final Agent agent, final AgentStateTickContext agentStateTickContext) {
        if (!agent.isHungry()) {
            return IdleAgentState.INSTANCE;
        }

        final Storage storage = agent.getBuildingStorageLocatedHere(agentStateTickContext.getGeoMap());
        if (storage == null) {
            return null;
        }

        final int retrieved = storage.retrieveToEmpty(ResourceType.FOOD, 1);
        if (retrieved == 0) {
            return IdleAgentState.INSTANCE;
        }

        agent.eat(retrieved);
        return null;
    }
}
