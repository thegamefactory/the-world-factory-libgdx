package com.tgf.twf.core.world.agents;

import com.tgf.twf.core.pathfinding.PathWalker;
import com.tgf.twf.core.world.storage.ResourceType;

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
        final boolean isHome = agent.isHome();

        if (agentStateTickContext.isNight() && !isHome) {
            return MoveToHomeAgentState.INSTANCE;
        }

        if (isHome && !agent.isStorageEmpty() && agent.getHomeStorage().acceptAny(agent.getStorage())) {
            return StoringFoodAgentState.INSTANCE;
        }

        if (agentStateTickContext.isNight() && isHome) {
            return SleepingAgentState.INSTANCE;
        }

        if (isHome && agent.isHungry() && agent.getHomeStorage().getStored(ResourceType.FOOD) > 0) {
            return EatingAgentState.INSTANCE;
        }

        if (isHome && agent.isStarving()) {
            // TODO: maybe handle case where the agent still has resources
            return StarvingAgentState.INSTANCE;
        }

        if (agent.isAnyStoredResourceFull()) {
            if (tryMoveToStorage(agent)) {
                return MoveToFoodStorageAgentState.INSTANCE;
            }
        }

        Action nextAction = agent.getAction();
        if (null == nextAction) {
            nextAction = agentStateTickContext.getAgentSystem().removeFirstAction();
        }

        if (nextAction != null) {
            final PathWalker pathWalker = PathWalker.createPathWalker(agent, nextAction.getPosition());
            if (pathWalker != null) {
                agent.setAction(nextAction);
                agent.setPathWalker(pathWalker);
                return MoveToActionAgentState.INSTANCE;
            } else {
                agentStateTickContext.getAgentSystem().addActionLast(nextAction);
            }
        }

        if (!agent.isStorageEmpty()) {
            if (tryMoveToStorage(agent)) {
                return MoveToFoodStorageAgentState.INSTANCE;
            }
        }

        return null;
    }

    private boolean tryMoveToStorage(final Agent agent) {
        // TODO - break assumption home == closest available storage
        // TODO - check that the picked storage can accept the resources before trying to go there (maybe?) or at least break the infinite
        //  cycle idle -> move-to-food-storage -> storing-food -> idle that happens when it's the case
        final PathWalker pathWalker = PathWalker.createPathWalker(agent, agent.getHomePosition());
        if (pathWalker != null) {
            agent.setPathWalker(pathWalker);
            return true;
        }
        return false;
    }
}
