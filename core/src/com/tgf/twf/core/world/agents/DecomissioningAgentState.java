package com.tgf.twf.core.world.agents;

/**
 * A {@link AgentState} in which the {@link Agent} becomes decommissioned.
 */
public class DecomissioningAgentState implements AgentState {
    private DecomissioningAgentState() {

    }

    public static DecomissioningAgentState INSTANCE = new DecomissioningAgentState();

    @Override
    public AgentState tick(final Agent agent, final AgentStateTickContext agentStateTickContext) {
        // this is never invoked as the agent gets removed before this state ticks
        return null;
    }
}
