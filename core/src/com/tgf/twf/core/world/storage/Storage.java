package com.tgf.twf.core.world.storage;

import com.tgf.twf.core.ecs.Component;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A component to model a capacity to store resources and the actual quantity of stored resources.
 * The capacity of the storage is modeled in {@link Capacity}, while the actual stock is modeled in the {@link Inventory}.
 */
@RequiredArgsConstructor
public class Storage extends Component {
    private final MutableInventory inventory = new MutableInventory();
    private final Capacity capacity;

    /**
     * @param offeredQuantity The quantity offered to the storage.
     * @param resourceType    The type of resource offered to the storage.
     * @return The number of stored resource. Will be equals to the quantity parameter if the storage capacity allows.
     */
    public int store(final ResourceType resourceType, final int offeredQuantity) {
        int stored = 0;
        final int remainingCapacity = capacity.getRemainingCapacity(inventory, resourceType);
        if (remainingCapacity > 0) {
            stored = Math.min(remainingCapacity, offeredQuantity);
            inventory.store(resourceType, stored);
        }
        return stored;
    }

    /**
     * Force to store the given quantity of {@link ResourceType}, even if it overflows the capacity.
     *
     * @param quantity     The quantity offered to the storage.
     * @param resourceType The type of resource offered to the storage.
     */
    public void forceStore(final ResourceType resourceType, final int quantity) {
        inventory.store(resourceType, quantity);
    }

    /**
     * @param resources The quantity of resources to consume, per resource type.
     * @return true if the storage had enough resources in store to satisfy the demand. If true, the resources are removed from the storage. If
     * false, the storage is left untouched.
     */
    public boolean tryConsumeResources(final Inventory resources) {
        final boolean canConsume = resources.getStoredResourceTypes().stream()
                .allMatch(resourceType -> inventory.getStoredQuantity(resourceType) >= resources.getStoredQuantity(resourceType));
        if (canConsume) {
            resources.getStoredResourceTypes()
                    .forEach(resourceType -> inventory.retrieve(resourceType, resources.getStoredQuantity(resourceType)));
        }
        return canConsume;
    }

    public void transfer(final Storage destinationStorage) {
        final Set<ResourceType> emptyResourceTypes = new HashSet<>();
        inventory.stock.forEach((key, value) -> {
            final int actuallyStored = destinationStorage.store(key, value);
            final boolean isEmpty = inventory.retrieve(key, actuallyStored, false);
            if (isEmpty) {
                emptyResourceTypes.add(key);
            }
        });
        emptyResourceTypes.forEach(inventory.stock::remove);
    }

    /**
     * Defines a storage capacity.
     * The capacity defines the quantity of resources of a {@link ResourceType} that a given {@link Inventory} can stock, on top of the resources that
     * the {@link Inventory} already has in stock.
     */
    @FunctionalInterface
    public interface Capacity {
        int getRemainingCapacity(final Inventory currentInventory, final ResourceType resourceType);
    }

    /**
     * An interface to represent the inventory of the {@link Storage}.
     */
    public interface Inventory {
        int getStoredQuantity(ResourceType resourceType);

        int getTotalStoredQuantity();

        Set<ResourceType> getStoredResourceTypes();
    }

    public static class MutableInventory implements Inventory {
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

        private void retrieve(final ResourceType resourceType, final int quantity) {
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
}
