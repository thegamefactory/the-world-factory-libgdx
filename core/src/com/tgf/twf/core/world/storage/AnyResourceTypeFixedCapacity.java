package com.tgf.twf.core.world.storage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AnyResourceTypeFixedCapacity implements Storage.Capacity {
    final int capacity;

    @Override
    public int getRemainingCapacity(final Storage.Inventory currentInventory, final ResourceType resourceType) {
        return capacity - currentInventory.getTotalStoredQuantity();
    }
}
