package com.tgf.twf.core.world.agents;

/**
 * A {@link AgentState} in which the {@link Agent} moves to an {@link Action} position.
 */
public class MoveToActionAgentState implements AgentState {
    private MoveToActionAgentState() {

    }

    public static MoveToActionAgentState INSTANCE = new MoveToActionAgentState();

    @Override
    public AgentState tick(final Agent agent, final AgentStateTickContext agentStateTickContext) {
        if (agent.getPathWalker().walk()) {
            agent.setPathWalker(null);
            return ExecuteActionAgentState.INSTANCE;
        }

        return null;
    }
}
