package com.tgf.twf.core.world.storage;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * Inventory for immutable 0 resources.
 */
public class EmptyInventory implements Inventory {
    private EmptyInventory() {
    }

    public static EmptyInventory INSTANCE = new EmptyInventory();

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public int getStoredQuantity(final ResourceType resourceType) {
        return 0;
    }

    @Override
    public int getTotalStoredQuantity() {
        return 0;
    }

    @Override
    public Set<ResourceType> getStoredResourceTypes() {
        return ImmutableSet.of();
    }

    @Override
    public int getReservedQuantity(final ResourceType resourceType) {
        return 0;
    }

    @Override
    public int getTotalReservedQuantity() {
        return 0;
    }
}
