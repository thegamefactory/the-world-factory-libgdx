package com.tgf.twf.core.world.agents;

/**
 * Base interface to model a state in the {@link Agent} state machine.
 * An agent acts according to its current state and transitions to other states according to the current state.
 * {@link AgentState} implementations should follow the singleton pattern.
 */
public interface AgentState {
    /**
     * @param agent                 The agent in this state.
     * @param agentStateTickContext Some general game context available to the state.
     * @return the next state, if there's any state transition, or null otherwise.
     */
    AgentState tick(Agent agent, AgentStateTickContext agentStateTickContext);
}
