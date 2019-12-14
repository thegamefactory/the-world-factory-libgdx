package com.tgf.twf.core.world.storage;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Concrete implementation of {@link MutableInventory} based on a {@link HashMap} of {@link ResourceType} to integer quantity.
 */
public class HashMapInventory implements MutableInventory {
    @Data
    private static class Stock {
        private int storedQuantity;
        private int reservedQuantity;

        Stock(final int storedQuantity, final int reservedQuantity) {
            this.storedQuantity = storedQuantity;
            this.reservedQuantity = reservedQuantity;
        }

        static Stock ofStoredQuantity(final int storedQuantity) {
            return new Stock(storedQuantity, 0);
        }

        static Stock ofReservedQuantity(final int reservedQuantity) {
            return new Stock(0, reservedQuantity);
        }
    }

    private final Map<ResourceType, Stock> stocks = new HashMap<>();
    private int totalStoredQuantity = 0;

    @Override
    public boolean isEmpty() {
        return totalStoredQuantity == 0;
    }

    @Override
    public int getStoredQuantity(final ResourceType resourceType) {
        final Stock stock = stocks.get(resourceType);
        return stock == null ? 0 : stock.storedQuantity;
    }

    @Override
    public int getReservedQuantity(final ResourceType resourceType) {
        final Stock stock = stocks.get(resourceType);
        return stock == null ? 0 : stock.reservedQuantity;
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
        Stock stock = stocks.get(resourceType);
        if (null == stock) {
            stock = Stock.ofStoredQuantity(quantity);
            stocks.put(resourceType, stock);
        } else {
            stock.storedQuantity += quantity;
            stock.reservedQuantity = Math.max(stock.reservedQuantity - quantity, 0);
        }
        totalStoredQuantity += quantity;
        return true;
    }

    @Override
    public boolean store(final Inventory inventory) {
        for (final ResourceType resourceType : inventory.getStoredResourceTypes()) {
            store(resourceType, inventory.getStoredQuantity(resourceType));
        }
        return true;
    }

    @Override
    public boolean reserve(final ResourceType resourceType, final int quantity) {
        if (quantity == 0) {
            return true;
        }
        final Stock stock = stocks.get(resourceType);
        if (stock == null) {
            stocks.put(resourceType, Stock.ofReservedQuantity(quantity));
        } else {
            stock.reservedQuantity += quantity;
        }
        return true;
    }

    @Override
    public boolean reserve(final Inventory inventory) {
        inventory.getStoredResourceTypes().forEach(
                resourceType -> reserve(resourceType, inventory.getStoredQuantity(resourceType))
        );
        return true;
    }

    @Override
    public boolean retrieve(final ResourceType resourceType, final int quantity) {
        if (quantity == 0) {
            return true;
        }
        final Stock stock = stocks.get(resourceType);
        if (null == stock) {
            return false;
        }
        final int newStock = stock.storedQuantity - quantity;
        if (newStock < 0) {
            return false;
        }
        if (newStock == 0 && stock.reservedQuantity == 0) {
            stocks.remove(resourceType);
        } else {
            stock.storedQuantity = newStock;
        }
        totalStoredQuantity -= quantity;
        return true;
    }

    @Override
    public boolean retrieve(final Inventory inventory) {
        inventory.getStoredResourceTypes().forEach(
                resourceType -> retrieve(resourceType, inventory.getStoredQuantity(resourceType))
        );
        return true;
    }

    @Override
    public void clear() {
        stocks.clear();
        totalStoredQuantity = 0;
    }
}
