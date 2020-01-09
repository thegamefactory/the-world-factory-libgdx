package com.tgf.twf.core.world.storage;

import com.tgf.twf.core.ecs.Component;
import lombok.Getter;

/**
 * A component to model a capacity to store resources and the actual quantity of stored resources.
 * The capacity of the storage is modeled in {@link Capacity}, while the actual stock is modeled in the {@link Inventory}.
 */
public class Storage extends Component {
    private MutableInventory inventory;

    @Getter
    private Capacity capacity;

    public Storage() {
        this(NoCapacity.INSTANCE);
    }

    public Storage(final Capacity capacity) {
        this.capacity = capacity;
        this.inventory = capacity.buildMutableInventory();
    }

    public void setCapacity(final Capacity newCapacity) {
        if (newCapacity.getClass() != capacity.getClass()) {
            if (inventory.getTotalStoredQuantity() != 0) {
                throw new IllegalStateException("Cannot change capacity class, inventory is not empty");
            }
            this.inventory = newCapacity.buildMutableInventory();
        }
        this.capacity = newCapacity;
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
     * @return True if the {@link Inventory} contains any resource for which there's no storage available anymore.
     */
    public boolean isAnyResourceFull() {
        return inventory.getStoredResourceTypes().stream()
                .anyMatch(resourceType -> capacity.getRemainingCapacity(inventory, resourceType) <= 0);
    }

    /**
     * Store the resources in the inventory, up to the storage capacity.
     *
     * @param resourceType The type of resources to store.
     * @param quantity     The qu
     *                     of resources to store.
     * @return the quantity actually stored.
     */
    public int storeToCapacity(final ResourceType resourceType, final int quantity) {
        final int remainingCapacity = capacity.getRemainingCapacity(inventory, resourceType);
        final int actuallyStored = Math.min(quantity, remainingCapacity);
        if (!inventory.store(resourceType, actuallyStored)) {
            throw new IllegalStateException("Storage rejected storage of " + quantity + " " + resourceType);
        }
        return actuallyStored;
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
     * Retrieve the requested quantity of resources, up to emptying the inventory completely.
     *
     * @param resourceType The type of resources to retrieve.
     * @param quantity     The quantity of resources to retrieve.
     * @return the quantity actually retrieved.
     */
    public int retrieveToEmpty(final ResourceType resourceType, final int quantity) {
        final int availableQuantity = inventory.getStoredQuantity(resourceType);
        final int actuallyRetrieved = Math.min(quantity, availableQuantity);

        if (!inventory.retrieve(resourceType, actuallyRetrieved)) {
            throw new IllegalStateException("Storage rejected retrieval of " + actuallyRetrieved + " " + resourceType);
        }

        return actuallyRetrieved;
    }

    /**
     * @param other The tested {@link Storage}
     * @return True if any of the resource types contained in the other storage can be stored in this storage.
     */
    public boolean acceptAny(final Storage other) {
        return other.inventory.getStoredResourceTypes().stream()
                .anyMatch(resourceType -> getCapacity().getRemainingCapacity(inventory, resourceType) > 0);
    }

    @Override
    public String toString() {
        return super.toString() + "(capacity=" + capacity + ", inventory=" + inventory + ")";
    }
}
