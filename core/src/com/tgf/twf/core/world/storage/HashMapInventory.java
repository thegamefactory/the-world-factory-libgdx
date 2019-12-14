package com.tgf.twf.core.world.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Concrete implementation of {@link MutableInventory} based on a {@link HashMap} of {@link ResourceType} to integer quantity.
 */
public class HashMapInventory implements MutableInventory {
    private final Map<ResourceType, Integer> stock = new HashMap<>();
    private int totalStoredQuantity = 0;

    @Override
    public int getStoredQuantity(final ResourceType resourceType) {
        return stock.getOrDefault(resourceType, 0);
    }

    @Override
    public int getTotalStoredQuantity() {
        return totalStoredQuantity;
    }

    @Override
    public Set<ResourceType> getStoredResourceTypes() {
        return stock.keySet();
    }

    @Override
    public boolean store(final ResourceType resourceType, final int quantity) {
        stock.put(resourceType, stock.getOrDefault(resourceType, 0) + quantity);
        totalStoredQuantity += quantity;
        return true;
    }

    @Override
    public boolean store(final Inventory inventory) {
        boolean result = true;
        for (final ResourceType resourceType : inventory.getStoredResourceTypes()) {
            result &= store(resourceType, inventory.getStoredQuantity(resourceType));
        }
        return result;
    }


    @Override
    public boolean retrieve(final ResourceType resourceType, final int quantity) {
        final int currentStock = stock.getOrDefault(resourceType, 0);
        final int newStock = currentStock - quantity;
        if (newStock < 0) {
            return false;
        }
        if (newStock == 0) {
            stock.remove(resourceType);
        } else {
            stock.put(resourceType, newStock);
        }
        totalStoredQuantity -= quantity;
        return true;
    }

    @Override
    public void clear() {
        stock.clear();
        totalStoredQuantity = 0;
    }
}
