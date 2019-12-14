package com.tgf.twf.core.world.storage;

import java.util.Set;

/**
 * An interface to represent an inventory of resources.
 */
public interface Inventory {
    int getStoredQuantity(ResourceType resourceType);

    int getTotalStoredQuantity();

    Set<ResourceType> getStoredResourceTypes();
}
