package com.tgf.twf.core.world.storage;

import java.util.Set;

/**
 * An interface to represent an inventory of resources.
 */
public interface Inventory {
    /**
     * @return true if the inventory is empty.
     */
    boolean isEmpty();

    /**
     * @param resourceType the given {@link ResourceType}.
     * @return the quantity of resources stored for the given {@link ResourceType}
     * If there's corresponding reserved capacity on the resource type, it will be deallocated.
     */
    int getStoredQuantity(ResourceType resourceType);

    /**
     * @return the total quantity of resources stored across all {@link ResourceType}s.
     */
    int getTotalStoredQuantity();

    /**
     * @return all the {@link ResourceType}s with a non-zero stored quantity.
     */
    Set<ResourceType> getStoredResourceTypes();
}
