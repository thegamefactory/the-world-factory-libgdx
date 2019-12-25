package com.tgf.twf.core.world.agents;

/**
 * A {@link AgentState} in which the {@link Agent} moves to an food source position.
 */
public class MoveToFoodSourceAgentState implements AgentState {
    private MoveToFoodSourceAgentState() {

    }

    public static MoveToFoodSourceAgentState INSTANCE = new MoveToFoodSourceAgentState();

    @Override
    public AgentState tick(final Agent agent, final AgentStateTickContext agentStateTickContext) {
        if (agent.getPathWalker().walk()) {
            agent.setPathWalker(null);
            return RetrievingFoodAgentState.INSTANCE;
        }

        return null;
    }
}
