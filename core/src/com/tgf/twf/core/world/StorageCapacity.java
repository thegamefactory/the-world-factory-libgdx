package com.tgf.twf.core.world;

/**
 * Defines a storage capacity.
 * The capacity defines the quantity of resources of a {@link ResourceType }that a given {@link Storage} can stock, on top of the resources that
 * the storage already has in stock.
 */
@FunctionalInterface
public interface StorageCapacity {
    int getRemainingCapacity(final Storage currentStorage, final ResourceType resourceType);
}
