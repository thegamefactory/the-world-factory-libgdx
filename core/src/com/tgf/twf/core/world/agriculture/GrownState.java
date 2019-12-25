package com.tgf.twf.core.world.agriculture;

import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.agents.Action;
import com.tgf.twf.core.world.agents.Agent;
import com.tgf.twf.core.world.agents.CyclicAction;
import com.tgf.twf.core.world.agents.TaskSystem;
import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.storage.Storage;
import lombok.RequiredArgsConstructor;

/**
 * The {@link Field.State}  of that field when it's grown.
 * The {@link Field} will remain in that {@link Field.State} until an agent performs a harvest task on the {@link Field}, at which point it
 * will transition back to {@link UncultivatedState}.
 */
@RequiredArgsConstructor
public class GrownState implements Field.State {
    private boolean isComplete = false;
    private final TaskSystem taskSystem;
    private final Vector2 fieldPosition;
    private final Storage fieldStorage;

    @Override
    public Class<? extends Field.State> tick() {
        return isComplete ? UncultivatedState.class : null;
    }

    @Override
    public void onStateEnter() {
        fieldStorage.storeToCapacity(ResourceType.FOOD, Rules.FIELD_FOOD_YIELD);
        taskSystem.addActionLast(buildHarvestAction());
    }

    private Action buildHarvestAction() {
        return new HarvestAction();
    }

    @Override
    public String toString() {
        return "GrownState[isComplete=" + isComplete + "]";
    }

    class HarvestAction extends CyclicAction {
        public HarvestAction() {
            super(fieldPosition, Rules.HARVEST_ONE_FOOD_DURATION);
        }

        @Override
        public boolean onCycleComplete(final Agent agent) {
            final int retrieved = fieldStorage.retrieveToEmpty(ResourceType.FOOD, 1);
            final int stored = agent.getRelatedComponent(Storage.class).storeToCapacity(ResourceType.FOOD, retrieved);
            isComplete = fieldStorage.getStored(ResourceType.FOOD) <= 0;
            return isComplete;
        }
    }
}
