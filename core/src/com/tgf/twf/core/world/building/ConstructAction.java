package com.tgf.twf.core.world.building;

import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.core.world.storage.EmptyInventory;
import com.tgf.twf.core.world.storage.Inventory;
import com.tgf.twf.core.world.task.Action;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

/**
 * An {@link Action} implementation which constructs a {@link Building}.
 */
@RequiredArgsConstructor
public class ConstructAction implements Action {
    private final Building building;

    @Override
    public boolean isComplete() {
        return building.isConstructed();
    }

    @Override
    public void update(final Duration delta) {
        building.build(delta);
    }

    @Override
    public Inventory getCost() {
        return Rules.CONSTRUCT_COST;
    }

    @Override
    public Inventory getProduction() {
        return EmptyInventory.INSTANCE;
    }

    @Override
    public String getName() {
        return "construct";
    }
}
