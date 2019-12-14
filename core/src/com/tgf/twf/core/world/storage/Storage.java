package com.tgf.twf.core.world.storage;

import com.tgf.twf.core.ecs.Component;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

/**
 * A component to model a capacity to store resources and the actual quantity of stored resources.
 * The capacity of the storage is modeled in {@link Capacity}, while the actual stock is modeled in the {@link Inventory}.
 */
@RequiredArgsConstructor
public class Storage extends Component {
    private final MutableInventory inventory = new HashMapInventory();
    @Getter
    private final Capacity capacity;

    public int getStored(final ResourceType resourceType) {
        return inventory.getStoredQuantity(resourceType);
    }

    /**
     * @return True if the {@link Inventory} contains 0 resources.
     */
    public boolean isEmpty() {
        return inventory.getTotalStoredQuantity() == 0;
    }

    /**
     * @param quantity     The quantity added to the storage.
     * @param resourceType The type of resource offered to the storage.
     * @return The true if the resources are stored, if the capacity permits it.
     */
    public boolean store(final ResourceType resourceType, final int quantity) {
        final int remainingCapacity = capacity.getRemainingCapacity(inventory, resourceType);
        if (remainingCapacity >= quantity) {
            if (!inventory.store(resourceType, quantity)) {
                throw new IllegalStateException("Storage unexpectedly rejected " + quantity + " " + resourceType);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Force to store the given quantity of {@link ResourceType}, even if it overflows the capacity.
     *
     * @param quantity     The quantity offered to the storage.
     * @param resourceType The type of resource offered to the storage.
     */
    public void forceStore(final ResourceType resourceType, final int quantity) {
        if (!inventory.store(resourceType, quantity)) {
            throw new IllegalStateException("Inventory rejected storage");
        }
    }

    /**
     * @param resourceType The resource type whose capacity is being queried.
     * @return The remaining capacity of the storage for this capacity.
     */
    public int getRemainingCapacity(final ResourceType resourceType) {
        return capacity.getRemainingCapacity(inventory, resourceType);
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
                    .forEach(resourceType -> {
                        if (!inventory.retrieve(resourceType, resources.getStoredQuantity(resourceType))) {
                            throw new IllegalStateException("Storage rejected retrieval of " + resourceType);
                        }
                    });
        }
        return canConsume;
    }

    /**
     * Transfer the whole inventory to the destination storage.
     *
     * @return true if the inventory was transferred, depending on destination storage capacity.
     */
    public boolean transfer(final Storage destinationStorage) {
        if (inventory.getTotalStoredQuantity() == 0) {
            return true;
        }

        final boolean canStore = inventory.getStoredResourceTypes().stream()
                .allMatch(resourceType -> destinationStorage.getRemainingCapacity(resourceType) >= inventory.getStoredQuantity(resourceType));
        if (canStore) {
            inventory.getStoredResourceTypes()
                    .forEach(resourceType -> {
                        if (!destinationStorage.store(resourceType, inventory.getStoredQuantity(resourceType))) {
                            throw new IllegalStateException("Destination storage rejected " + resourceType);
                        }
                    });
            inventory.clear();
        }
        return canStore;
    }

    /**
     * Defines a storage capacity.
     * The capacity defines the quantity of resources of a {@link ResourceType} that a given {@link Inventory} can stock, on top of the resources that
     * the {@link Inventory} already has in stock.
     */
    public interface Capacity {
        int getRemainingCapacity(final Inventory currentInventory, final ResourceType resourceType);

        int getTotalCapacity(final ResourceType resourceType);

        Set<ResourceType> getStorableResourceTypes();
    }

}
