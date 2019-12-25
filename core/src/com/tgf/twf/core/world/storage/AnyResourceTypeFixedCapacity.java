package com.tgf.twf.core.world.storage;

import com.google.common.collect.ImmutableSet;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Set;

/**
 * {@link Capacity} which is agnostic of resource type i.e. treats all resource types the same.
 */
@RequiredArgsConstructor
@ToString
public class AnyResourceTypeFixedCapacity implements Capacity {
    final int capacity;
    private static final Set<ResourceType> STORABLE_RESOURCE_TYPES = ImmutableSet.copyOf(ResourceType.values());

    @Override
    public int getRemainingCapacity(final Inventory currentInventory, final ResourceType resourceType) {
        final int currentInventoryUsage = currentInventory.getTotalStoredQuantity();
        return Math.max(capacity - currentInventoryUsage, 0);
    }

    @Override
    public int getTotalCapacity(final ResourceType resourceType) {
        return capacity;
    }

    @Override
    public Set<ResourceType> getStorableResourceTypes() {
        return STORABLE_RESOURCE_TYPES;
    }

    @Override
    public MutableInventory buildMutableInventory() {
        return new HashMapInventory();
    }
}
