package com.tgf.twf.core.world.building;

import com.tgf.twf.core.geo.Position;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.agents.Action;
import com.tgf.twf.core.world.agents.Agent;
import com.tgf.twf.core.world.rules.Rules;
import lombok.RequiredArgsConstructor;

/**
 * An {@link Action} implementation which constructs a {@link Building}.
 */
@RequiredArgsConstructor
public class ConstructAction implements Action {
    private final Building building;
    private int investedResources;
    private int tickCount = 0;

    @Override
    public Vector2 getPosition() {
        return building.getRelatedComponent(Position.class).toVector2();
    }

    @Override
    public boolean tick(final Agent agent) {
        if (tickCount % Rules.CONSTRUCT_DURATION_PER_RESOURCE == 0) {
            if (investedResources < building.getBuildingType().getCostQuantity()) {
                final int retrieved = agent.retrieve(building.getBuildingType().getCostResourceType(), 1);
                if (retrieved > 0) {
                    investedResources += retrieved;
                    tickCount++;
                }
                // else resource starvation; TODO: handle this case
            } else {
                building.setConstructed();
                return true;
            }
        } else {
            tickCount++;
        }
        return false;
    }
}
