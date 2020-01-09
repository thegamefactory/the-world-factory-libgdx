package com.tgf.twf.core.world.agents;

public class SleepingAgentState implements AgentState {
    private SleepingAgentState() {

    }

    public static SleepingAgentState INSTANCE = new SleepingAgentState();

    @Override
    public AgentState tick(final Agent agent, final AgentStateTickContext agentStateTickContext) {
        if (agentStateTickContext.isDay()) {
            return IdleAgentState.INSTANCE;
        }

        return null;
    }
}
