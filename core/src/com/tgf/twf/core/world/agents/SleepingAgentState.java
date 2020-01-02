package com.tgf.twf.core.world.agents;

import com.tgf.twf.core.world.daytimesystem.Daytime;

public class SleepingAgentState implements AgentState {
    private SleepingAgentState() {

    }

    public static SleepingAgentState INSTANCE = new SleepingAgentState();

    @Override
    public AgentState tick(final Agent agent, final AgentStateTickContext agentStateTickContext) {
        if (Daytime.INSTANCE.isDay()) {
            return IdleAgentState.INSTANCE;
        }

        return null;
    }
}
