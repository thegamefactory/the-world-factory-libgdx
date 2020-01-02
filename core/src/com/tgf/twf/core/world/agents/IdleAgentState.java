package com.tgf.twf.core.world.agents;

import com.tgf.twf.core.pathfinding.PathWalker;

/**
 * A {@link AgentState} in which the {@link Agent} rests and replenishes its energy slowly.
 * In this state the agent will attempt to transition to states to satisfy its food needs, store the resource it's carrying, and get a new action
 * to execute.
 */
public class IdleAgentState implements AgentState {
    private IdleAgentState() {
    }

    public static IdleAgentState INSTANCE = new IdleAgentState();

    @Override
    public AgentState tick(final Agent agent, final AgentStateTickContext agentStateTickContext) {
        if (agent.isAnyStoredResourceFull()) {
            // TODO - break assumption home == closest available storage
            // TODO - check that the picked storage can accept the resources before trying to go there (maybe?) or at least break the infinite
            //  cycle idle -> move-to-food-storage -> storing-food -> idle that happens when it's the case
            final PathWalker pathWalker = PathWalker.createPathWalker(agent, agent.getHomePosition());
            if (pathWalker != null) {
                agent.setPathWalker(pathWalker);
                return MoveToFoodStorageAgentState.INSTANCE;
            }
        }

        Action nextAction = agent.getAction();
        if (null == nextAction) {
            nextAction = agentStateTickContext.getTaskSystem().removeFirstAction();
        }

        if (nextAction != null) {
            final PathWalker pathWalker = PathWalker.createPathWalker(agent, nextAction.getPosition());
            if (pathWalker != null) {
                agent.setAction(nextAction);
                agent.setPathWalker(pathWalker);
                return MoveToActionAgentState.INSTANCE;
            } else {
                agentStateTickContext.getTaskSystem().addActionLast(nextAction);
            }
        }

        return null;
    }
}
