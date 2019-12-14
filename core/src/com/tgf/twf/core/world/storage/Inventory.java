package com.tgf.twf.core.world.storage;

import java.util.Set;

/**
 * An interface to represent the inventory of the {@link Storage}.
 */
public interface Inventory {
    int getStoredQuantity(ResourceType resourceType);

    int getTotalStoredQuantity();

    Set<ResourceType> getStoredResourceTypes();
}
