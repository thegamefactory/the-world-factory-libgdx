package com.tgf.twf.core.world.storage;

import com.tgf.twf.core.ecs.Component;
import lombok.Getter;

/**
 * A component to model a capacity to store resources and the actual quantity of stored resources.
 * The capacity of the storage is modeled in {@link Capacity}, while the actual stock is modeled in the {@link Inventory}.
 */
public class Storage extends Component {
    private final MutableInventory inventory;
    @Getter
    private final Capacity capacity;

    public Storage(final Capacity capacity) {
        this.capacity = capacity;
        this.inventory = capacity.buildMutableInventory();
    }

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
     * @param resourceType      The resource type whose capacity is being queried.
     * @param capacityCountMode Whether or not consider reservations to evaluate the remaining capacity.
     * @return The remaining capacity of the storage for this capacity.
     */
    public int getRemainingCapacity(final ResourceType resourceType, final Capacity.CapacityCountMode capacityCountMode) {
        return capacity.getRemainingCapacity(inventory, resourceType, capacityCountMode);
    }

    /**
     * @param resources The quantity of resources to store, per resource type.
     * @return true if the storage have enough capacity in store to satisfy the offer.
     */
    public boolean canStore(final Inventory resources) {
        return resources.getStoredResourceTypes().stream()
                .allMatch(resourceType -> capacity.getRemainingCapacity(inventory, resourceType, Capacity.CapacityCountMode.EXCLUDE_RESERATIONS)
                        >= resources.getStoredQuantity(resourceType));
    }

    /**
     * Store the resources in the inventory. {@link #canStore(Inventory)} should be called first to check if the storage can accept the offer or not.
     *
     * @param resources The quantity of resources to store.
     */
    public void store(final Inventory resources) {
        if (!inventory.store(resources)) {
            throw new IllegalStateException("Storage rejected storage of " + resources);
        }
    }

    /**
     * @param resources The quantity of resources to store, per resource type.
     * @return true if the storage have enough capacity in store to satisfy the offer.
     */
    public boolean canReserve(final Inventory resources) {
        return resources.getStoredResourceTypes().stream()
                .allMatch(resourceType -> capacity.getRemainingCapacity(inventory, resourceType, Capacity.CapacityCountMode.INCLUDE_RESERVATIONS)
                        >= resources.getStoredQuantity(resourceType));
    }

    /**
     * Reserve capacity in the storage for a future {@link #store(Inventory)} call. {@link #canReserve(Inventory)} should be called first to check
     * if the storage can accept the offer or not.
     *
     * @param resources The quantity of resources to reservation.
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
                .allMatch(resourceType -> destinationStorage.getRemainingCapacity(
                        resourceType, Capacity.CapacityCountMode.EXCLUDE_RESERATIONS) >= inventory.getStoredQuantity(resourceType));
        if (canStore) {
            destinationStorage.store(inventory);
            inventory.clear();
        }
        return canStore;
    }

    @Override
    public String toString() {
        return super.toString() + "(capacity=" + capacity + ", inventory=" + inventory + ")";
    }
}
