package com.tgf.twf.core.world.agents;

import com.tgf.twf.core.world.rules.Rules;

/**
 * A {@link AgentState} in which the {@link Agent} consumes its food and gets energy for that.
 */
public class EatingAgentState implements AgentState {
    private EatingAgentState() {

    }

    public static EatingAgentState INSTANCE = new EatingAgentState();

    @Override
    public AgentState tick(final Agent agent, final AgentStateTickContext agentStateTickContext) {
        if (agent.getFood() == 0 || agent.getEnergy() + Rules.FOOD_TO_ENERGY_CONVERSION > Rules.AGENT_MAX_ENERGY_LEVEL) {
            if (agent.getEnergy() > 0 && agent.getAction() != null) {
                return ExecuteActionAgentState.INSTANCE;
            } else {
                return IdleAgentState.INSTANCE;
            }
        }

        final int removedFood = agent.retrieveFood(1);
        agent.storeEnergy(removedFood * Rules.FOOD_TO_ENERGY_CONVERSION);
        return null;
    }
}
