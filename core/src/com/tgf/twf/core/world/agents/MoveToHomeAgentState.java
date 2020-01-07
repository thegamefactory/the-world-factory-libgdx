package com.tgf.twf.core.world.agents;

import com.tgf.twf.core.pathfinding.PathWalker;

public class MoveToHomeAgentState implements AgentState {
    private MoveToHomeAgentState() {

    }

    public static MoveToHomeAgentState INSTANCE = new MoveToHomeAgentState();

    @Override
    public AgentState tick(final Agent agent, final AgentStateTickContext agentStateTickContext) {
        if (agent.getPathWalker() == null) {
            final PathWalker pathWalker = PathWalker.createPathWalker(agent, agent.getHomePosition());
            agent.setPathWalker(pathWalker);
        }

        if (agent.getPathWalker() == null) {
            // TODO: handle stuck case
            return null;
        }

        if (agent.getPathWalker().walk()) {
            agent.setPathWalker(null);
            return IdleAgentState.INSTANCE;
        }

        return null;
    }
}
