package com.tgf.twf.core.world.storage;

import com.google.common.collect.ImmutableSet;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Set;

/**
 * {@link Capacity} which is can only accept a single resource type.
 */
@RequiredArgsConstructor
@ToString
public class SingleResourceTypeCapacity implements Capacity {
    private final ResourceType resourceType;
    private final int capacity;

    @Override
    public int getRemainingCapacity(final Inventory currentInventory, final ResourceType resourceType) {
        if (this.resourceType.equals(resourceType)) {
            final int currentInventoryUsage = currentInventory.getStoredQuantity(resourceType);
            return Math.max(capacity - currentInventoryUsage, 0);
        } else {
            return 0;
        }
    }

    @Override
    public int getTotalCapacity(final ResourceType resourceType) {
        if (this.resourceType.equals(resourceType)) {
            return capacity;
        } else {
            return 0;
        }
    }

    @Override
    public Set<ResourceType> getStorableResourceTypes() {
        return ImmutableSet.of(ResourceType.FOOD);
    }

    @Override
    public MutableInventory buildMutableInventory() {
        return SingleResourceTypeInventory.empty(resourceType);
    }
}
