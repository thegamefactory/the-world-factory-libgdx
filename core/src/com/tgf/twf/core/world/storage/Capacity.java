package com.tgf.twf.core.world.storage;

import java.util.Set;

/**
 * Defines a {@link Storage} capacity.
 * The capacity defines the quantity of resources of a {@link ResourceType} that a given {@link Inventory} can stock, on top of the resources that
 * the {@link Inventory} already has in stock.
 */
public interface Capacity {
    enum CapacityCountMode {
        INCLUDE_RESERVATIONS,
        EXCLUDE_RESERATIONS
    }

    int getRemainingCapacity(final Inventory currentInventory, final ResourceType resourceType, final CapacityCountMode capacityCountMode);

    int getTotalCapacity(final ResourceType resourceType);

    Set<ResourceType> getStorableResourceTypes();

    MutableInventory buildMutableInventory();
}