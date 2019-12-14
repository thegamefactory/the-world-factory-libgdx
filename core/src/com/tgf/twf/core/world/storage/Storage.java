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
        return inventory.isEmpty();
    }

    /**
     * Clears the {@link Inventory}.
     */
    public void clear() {
        inventory.clear();
    }

    /**
     * @param resourceType The resource type whose capacity is being queried.
     * @return The remaining capacity of the storage for this capacity.
     */
    public int getRemainingCapacity(final ResourceType resourceType) {
        return capacity.getRemainingCapacity(inventory, resourceType);
    }

    /**
     * @param resources The quantity of resources to store, per resource type.
     * @return true if the storage have enough capacity in store to satisfy the offer.
     */
    public boolean canStore(final Inventory resources) {
        return resources.getStoredResourceTypes().stream()
                .allMatch(resourceType -> capacity.getRemainingCapacity(inventory, resourceType) >= resources.getStoredQuantity(resourceType));
    }

    /**
     * @param resources The quantity of resources to store. {@link #canStore(Inventory)} (Inventory)} should be called first to check if the
     *                  storage can accept the offer or not. {@link Capacity} overflow will throw an {@link IllegalStateException}.
     */
    public void store(final Inventory resources) {
        if (!inventory.store(resources)) {
            throw new IllegalStateException("Storage rejected storage of " + resources);
        }
    }

    /**
     * @param resources The quantity of resources to reservation. {@link #canStore(Inventory)} (Inventory)} should be called first to check if the
     *                  storage can accept the offer or not. {@link Capacity} overflow will throw an {@link IllegalStateException}.
     *                  The effect of this call is to reserve capacity in the storage for a future {@link #store(Inventory)} call.
     */
    public void reserve(final Inventory resources) {
        if (!inventory.reserve(resources)) {
            throw new IllegalStateException("Storage rejected reservation of " + resources);
        }
    }


    /**
     * Force to store the given quantity of {@link ResourceType}, regardless of the {@link Capacity}.
     *
     * @param quantity     The quantity offered to the storage.
     * @param resourceType The type of resource offered to the storage.
     */
    public void forceStore(final ResourceType resourceType, final int quantity) {
        if (!inventory.store(resourceType, quantity)) {
            throw new IllegalStateException("Inventory rejected storage of " + quantity + " " + resourceType);
        }
    }

    /**
     * @param resources The quantity of resources to consume.
     * @return true if the storage have enough resources in store to satisfy the demand.
     */
    public boolean canRetrieve(final Inventory resources) {
        return resources.getStoredResourceTypes().stream()
                .allMatch(resourceType -> inventory.getStoredQuantity(resourceType) >= resources.getStoredQuantity(resourceType));
    }

    /**
     * @param resources The quantity of resources to consume. {@link #canRetrieve(Inventory)} should be called first to check if the storage can
     *                  satisfy the demand or not. Insufficient capacity will throw an {@link IllegalStateException}.
     */
    public void retrieve(final Inventory resources) {
        resources.getStoredResourceTypes()
                .forEach(resourceType -> {
                    if (!inventory.retrieve(resourceType, resources.getStoredQuantity(resourceType))) {
                        throw new IllegalStateException("Storage rejected retrieval of " + resourceType);
                    }
                });
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
            destinationStorage.store(inventory);
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
