package com.tgf.twf.core.world.agriculture;

import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.agents.Agent;
import com.tgf.twf.core.world.agents.AgentSystem;
import com.tgf.twf.core.world.agents.CyclicAction;
import com.tgf.twf.core.world.rules.Rules;
import lombok.RequiredArgsConstructor;

/**
 * The {@link Field.State}  of that field when it's uncultivated.
 * The {@link Field} will remain in that {@link Field.State} until an agent performs an plant on the {@link Field}, at which point it
 * will transition back to {@link UncultivatedState}.
 */
@RequiredArgsConstructor
public class UncultivatedState implements Field.State {
    private boolean isComplete = false;
    private final AgentSystem agentSystem;
    private final Vector2 fieldPosition;

    @Override
    public Class<? extends Field.State> tick() {
        return isComplete ? GrowingState.class : null;
    }

    @Override
    public void onStateEnter() {
        agentSystem.addActionLast(new PlantAction());
    }

    class PlantAction extends CyclicAction {
        PlantAction() {
            super(fieldPosition, Rules.PLANT_TOTAL_DURATION);
        }

        @Override
        public boolean onCycleComplete(final Agent agent) {
            isComplete = true;
            return isComplete;
        }
    }
}
