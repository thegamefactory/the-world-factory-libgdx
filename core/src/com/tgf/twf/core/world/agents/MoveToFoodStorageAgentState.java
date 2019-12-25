package com.tgf.twf.core.world.agents;

/**
 * A {@link AgentState} in which the {@link Agent} moves to a food storage position.
 */
public class MoveToFoodStorageAgentState implements AgentState {
    private MoveToFoodStorageAgentState() {

    }

    public static MoveToFoodStorageAgentState INSTANCE = new MoveToFoodStorageAgentState();

    @Override
    public AgentState tick(final Agent agent, final AgentStateTickContext agentStateTickContext) {
        if (agent.getPathWalker().walk()) {
            agent.setPathWalker(null);
            return StoringFoodAgentState.INSTANCE;
        }

        return null;
    }
}
