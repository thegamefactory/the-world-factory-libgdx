package com.tgf.twf.core.world.storage;

import java.util.Set;

/**
 * An interface to represent an inventory of resources.
 */
public interface Inventory {
    boolean isEmpty();

    int getStoredQuantity(ResourceType resourceType);

    int getTotalStoredQuantity();

    Set<ResourceType> getStoredResourceTypes();
}
