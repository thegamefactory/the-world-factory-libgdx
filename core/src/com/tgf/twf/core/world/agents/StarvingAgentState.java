package com.tgf.twf.core.world.agents;

import com.tgf.twf.core.ecs.Component;

/**
 * A {@link AgentState} in which the {@link Agent} starves and will be removed from the game.
 */
public class StarvingAgentState implements AgentState {
    private StarvingAgentState() {

    }

    public static StarvingAgentState INSTANCE = new StarvingAgentState();

    @Override
    public AgentState tick(final Agent agent, final AgentStateTickContext agentStateTickContext) {
        agent.notify(StarvingAgentEvent.INSTANCE);
        if (agent.getAction() != null) {
            agentStateTickContext.getAgentSystem().addActionLast(agent.getAction());
        }
        if (agent.getPathWalker() != null) {
            agent.getPathWalker().close();
        }
        return null;
    }

    public static class StarvingAgentEvent implements Component.Event {
        private StarvingAgentEvent() {

        }

        public static StarvingAgentEvent INSTANCE = new StarvingAgentEvent();
    }
}
