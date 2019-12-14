package com.tgf.twf.core.world.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MutableInventory implements Inventory {
    private final Map<ResourceType, Integer> stock = new HashMap<>();
    private int totalStoredQuantity = 0;

    @Override
    public int getStoredQuantity(final ResourceType resourceType) {
        return stock.getOrDefault(resourceType, 0);
    }

    @Override
    public int getTotalStoredQuantity() {
        return 0;
    }

    @Override
    public Set<ResourceType> getStoredResourceTypes() {
        return stock.keySet();
    }

    public void store(final ResourceType resourceType, final int quantity) {
        stock.put(resourceType, stock.getOrDefault(resourceType, 0) + quantity);
        totalStoredQuantity += quantity;
    }

    public void store(final Inventory inventory) {
        for (final ResourceType resourceType : inventory.getStoredResourceTypes()) {
            store(resourceType, inventory.getStoredQuantity(resourceType));
        }
    }

    public void transfer(final Storage destinationStorage) {
        final Set<ResourceType> emptyResourceTypes = new HashSet<>();
        stock.forEach((key, value) -> {
            final int actuallyStored = destinationStorage.store(key, value);
            final boolean isEmpty = retrieve(key, actuallyStored, false);
            if (isEmpty) {
                emptyResourceTypes.add(key);
            }
        });
        emptyResourceTypes.forEach(stock::remove);
    }

    public void retrieve(final ResourceType resourceType, final int quantity) {
        retrieve(resourceType, quantity, true);
    }

    /**
     * @return true if there are no resources of the specified type left
     */
    private boolean retrieve(final ResourceType resourceType, final int quantity, final boolean removeEntry) {
        final int currentStock = stock.getOrDefault(resourceType, 0);
        final int newStock = currentStock - quantity;
        if (newStock < 0) {
            throw new IllegalStateException("Cannot retrieve " + quantity + " of " + resourceType + "; only " + currentStock + " available");
        }
        if (newStock == 0 && removeEntry) {
            stock.remove(resourceType);
        } else {
            stock.put(resourceType, newStock);
        }
        totalStoredQuantity -= quantity;
        return newStock == 0;
    }

    public void clear() {
        stock.clear();
    }
}
