package com.tgf.twf.core.world.storage;

import com.google.common.collect.ImmutableSet;
import lombok.RequiredArgsConstructor;

import java.util.Set;

/**
 * {@link Storage.Capacity} which is agnostic of resource type i.e. treats all resource types the same.
 */
@RequiredArgsConstructor
public class AnyResourceTypeFixedCapacity implements Storage.Capacity {
    final int capacity;
    private static final Set<ResourceType> STORABLE_RESOURCE_TYPES = ImmutableSet.copyOf(ResourceType.values());

    @Override
    public int getRemainingCapacity(final Inventory currentInventory, final ResourceType resourceType) {
        return capacity - currentInventory.getTotalStoredQuantity();
    }

    @Override
    public int getTotalCapacity(ResourceType resourceType) {
        return capacity;
    }

    @Override
    public Set<ResourceType> getStorableResourceTypes() {
        return STORABLE_RESOURCE_TYPES;
    }
}
