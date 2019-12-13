package com.tgf.twf.core.world.storage;

import lombok.RequiredArgsConstructor;

/**
 * {@link Storage.Capacity} which is agnostic of resource type i.e. treats all resource types the same.
 */
@RequiredArgsConstructor
public class AnyResourceTypeFixedCapacity implements Storage.Capacity {
    final int capacity;

    @Override
    public int getRemainingCapacity(final Storage.Inventory currentInventory, final ResourceType resourceType) {
        return capacity - currentInventory.getTotalStoredQuantity();
    }
}
