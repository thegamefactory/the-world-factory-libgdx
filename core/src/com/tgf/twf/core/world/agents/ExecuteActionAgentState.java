package com.tgf.twf.core.world.agents;

/**
 * A {@link AgentState} in which the {@link Agent} executes an {@link Action}.
 */
public class ExecuteActionAgentState implements AgentState {
    private ExecuteActionAgentState() {

    }

    public static ExecuteActionAgentState INSTANCE = new ExecuteActionAgentState();

    @Override
    public AgentState tick(final Agent agent, final AgentStateTickContext agentStateTickContext) {
        if (1 == agent.retrieveEnergy(1) && agent.getAction().tick(agent)) {
            agent.setAction(null);
            return IdleAgentState.INSTANCE;
        }

        if (agent.getEnergy() == 0) {
            return EatingAgentState.INSTANCE;
        }

        return null;
    }
}
