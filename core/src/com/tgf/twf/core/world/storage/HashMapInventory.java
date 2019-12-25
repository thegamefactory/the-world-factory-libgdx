package com.tgf.twf.core.world.storage;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Concrete implementation of {@link MutableInventory} based on a {@link HashMap} of {@link ResourceType} to integer quantity.
 */
@ToString
public class HashMapInventory implements MutableInventory {
    private final Map<ResourceType, Integer> stocks = new HashMap<>();
    private int totalStoredQuantity = 0;
    private final int totalReservedQuantity = 0;

    @Override
    public boolean isEmpty() {
        return totalStoredQuantity == 0;
    }

    @Override
    public int getStoredQuantity(final ResourceType resourceType) {
        return stocks.getOrDefault(resourceType, 0);
    }

    @Override
    public int getTotalStoredQuantity() {
        return totalStoredQuantity;
    }

    @Override
    public Set<ResourceType> getStoredResourceTypes() {
        return stocks.keySet();
    }

    @Override
    public boolean store(final ResourceType resourceType, final int quantity) {
        if (quantity == 0) {
            return true;
        }
        stocks.put(resourceType, stocks.getOrDefault(resourceType, 0) + quantity);
        totalStoredQuantity += quantity;
        return true;
    }

    @Override
    public boolean retrieve(final ResourceType resourceType, final int quantity) {
        if (quantity == 0) {
            return true;
        }
        final int stored = getStoredQuantity(resourceType);
        if (stored >= quantity) {
            stocks.put(resourceType, stored - quantity);
            totalStoredQuantity -= quantity;
            return true;
        } else {
            return false;
        }
    }
}
