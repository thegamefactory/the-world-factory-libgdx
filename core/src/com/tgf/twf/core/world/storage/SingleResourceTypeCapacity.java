package com.tgf.twf.core.world.storage;

import com.google.common.collect.ImmutableSet;
import lombok.RequiredArgsConstructor;

import java.util.Set;

/**
 * {@link Capacity} which is can only accept a single resource type.
 */
@RequiredArgsConstructor
public class SingleResourceTypeCapacity implements Capacity {
    private final ResourceType resourceType;
    private final int capacity;

    @Override
    public int getRemainingCapacity(final Inventory currentInventory, final ResourceType resourceType) {
        if (this.resourceType.equals(resourceType)) {
            return Math.max(capacity - currentInventory.getStoredQuantity(resourceType), 0);
        } else {
            return 0;
        }
    }

    @Override
    public int getTotalCapacity(final ResourceType resourceType) {
        if (resourceType.equals(resourceType)) {
            return capacity;
        } else {
            return 0;
        }
    }

    @Override
    public Set<ResourceType> getStorableResourceTypes() {
        return ImmutableSet.of(ResourceType.FOOD);
    }
}
