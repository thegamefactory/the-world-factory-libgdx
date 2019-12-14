package com.tgf.twf.core.world.storage;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * Inventory for a mutable quantity of a single {@link ResourceType}.
 */
public class SingleResourceTypeInventory implements MutableInventory {
    public static final Inventory ONE_FOOD = SingleResourceTypeInventory.of(ResourceType.FOOD, 1);

    private final ResourceType resourceType;
    private int quantity;
    private int reservedQuantity = 0;

    public SingleResourceTypeInventory(final ResourceType resourceType, final int quantity) {
        this.resourceType = resourceType;
        this.quantity = quantity;
    }

    public static SingleResourceTypeInventory of(final ResourceType resourceType, final int quantity) {
        return new SingleResourceTypeInventory(resourceType, quantity);
    }

    @Override
    public boolean store(final ResourceType resourceType, final int quantity) {
        if (quantity == 0) {
            return true;
        }
        if (resourceType == this.resourceType) {
            this.quantity += quantity;
            reservedQuantity = Math.max(0, reservedQuantity - quantity);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean store(final Inventory inventory) {
        if (checkInventoryContainsOnlyResourceType(inventory)) {
            return store(resourceType, inventory.getStoredQuantity(resourceType));
        } else {
            return false;
        }
    }

    @Override
    public boolean reserve(final ResourceType resourceType, final int quantity) {
        if (quantity == 0) {
            return true;
        }
        if (resourceType != this.resourceType) {
            return false;
        }
        this.reservedQuantity += quantity;
        return true;
    }

    @Override
    public boolean reserve(final Inventory inventory) {
        return false;
    }

    @Override
    public boolean retrieve(final ResourceType resourceType, final int quantity) {
        if (quantity == 0) {
            return true;
        }
        if (resourceType != this.resourceType) {
            return false;
        }
        if (quantity >= this.quantity - this.reservedQuantity) {
            this.quantity -= quantity;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean retrieve(final Inventory inventory) {
        if (checkInventoryContainsOnlyResourceType(inventory)) {
            return retrieve(resourceType, inventory.getStoredQuantity(resourceType));
        } else {
            return false;
        }
    }

    @Override
    public void clear() {
        this.quantity = 0;
        this.reservedQuantity = 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public int getStoredQuantity(final ResourceType resourceType) {
        if (resourceType == this.resourceType) {
            return quantity;
        } else {
            return 0;
        }
    }

    @Override
    public int getReservedQuantity(final ResourceType resourceType) {
        if (resourceType == this.resourceType) {
            return reservedQuantity;
        } else {
            return 0;
        }
    }

    @Override
    public int getTotalStoredQuantity() {
        return quantity;
    }

    @Override
    public Set<ResourceType> getStoredResourceTypes() {
        return ImmutableSet.of(resourceType);
    }

    private boolean checkInventoryContainsOnlyResourceType(final Inventory inventory) {
        for (final ResourceType resourceType : inventory.getStoredResourceTypes()) {
            if (resourceType != this.resourceType && inventory.getStoredQuantity(resourceType) > 0) {
                return false;
            }
        }
        return true;
    }
}
