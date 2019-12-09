package com.tgf.twf.core.world;

import com.tgf.twf.core.ecs.Component;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
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

    public int getStoredQuantity(final ResourceType resourceType) {
        return inventory.getStoredQuantity(resourceType);
    }

    /**
     * @param offeredQuantity The quantity offered to the storage.
     * @param resourceType    The type of resource offered to the storage.
     * @return The number of stored resource. Will be equals to the quantity parameter if the storage capacity allows.
     */
    public int store(final ResourceType resourceType, final int offeredQuantity) {
        int stored = 0;
        final int remainingCapacity = capacity.getRemainingCapacity(this, resourceType);
        if (remainingCapacity > 0) {
            stored = Math.min(remainingCapacity, offeredQuantity);
            inventory.store(resourceType, stored);
        }
        return stored;
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

    /**
     * Defines a storage capacity.
     * The capacity defines the quantity of resources of a {@link ResourceType }that a given {@link Storage} can stock, on top of the resources that
     * the storage already has in stock.
     */
    @FunctionalInterface
    public interface Capacity {
        int getRemainingCapacity(final Storage currentStorage, final ResourceType resourceType);
    }

    /**
     * An interface to represent the inventory of the {@link Storage}.
     */
    public interface Inventory {
        int getStoredQuantity(ResourceType resourceType);

        Set<ResourceType> getStoredResourceTypes();
    }

    public static class MutableInventory implements Inventory {
        private final Map<ResourceType, Integer> stock = new HashMap<>();

        @Override
        public int getStoredQuantity(final ResourceType resourceType) {
            return stock.getOrDefault(resourceType, 0);
        }

        @Override
        public Set<ResourceType> getStoredResourceTypes() {
            return stock.keySet();
        }

        public void store(final ResourceType resourceType, final int quantity) {
            this.stock.put(resourceType, this.stock.getOrDefault(resourceType, 0) + quantity);
        }

        public void store(final Inventory inventory) {
            for (final ResourceType resourceType : inventory.getStoredResourceTypes()) {
                store(resourceType, inventory.getStoredQuantity(resourceType));
            }
        }

        public void retrieve(final ResourceType resourceType, final int quantity) {
            final int currentStock = this.stock.getOrDefault(resourceType, 0);
            if (currentStock < quantity) {
                throw new IllegalStateException("Cannot retrieve " + quantity + " of " + resourceType + "; only " + currentStock + " available");
            }
            this.stock.put(resourceType, currentStock - quantity);
        }

        public void clear() {
            stock.clear();
        }
    }
}
