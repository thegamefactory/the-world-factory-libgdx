package com.tgf.twf.core.world.storage;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;

import java.util.Set;

/**
 * Inventory for a mutable quantity of a single {@link ResourceType}.
 */
@AllArgsConstructor
public class SingleResourceTypeInventory implements MutableInventory {
    public static final Inventory ONE_FOOD = SingleResourceTypeInventory.of(ResourceType.FOOD, 1);

    private final ResourceType resourceType;
    private int quantity;

    public static SingleResourceTypeInventory of(final ResourceType resourceType, final int quantity) {
        return new SingleResourceTypeInventory(resourceType, quantity);
    }

    @Override
    public boolean store(final ResourceType resourceType, final int quantity) {
        if (resourceType == this.resourceType) {
            this.quantity += quantity;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean store(final Inventory inventory) {
        final Set<ResourceType> inventoryResourceTypes = inventory.getStoredResourceTypes();
        if (Sets.difference(inventoryResourceTypes, getStoredResourceTypes()).isEmpty()) {
            this.quantity += inventory.getStoredQuantity(resourceType);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean retrieve(final ResourceType resourceType, final int quantity) {
        if (quantity >= this.quantity) {
            this.quantity -= quantity;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clear() {
        this.quantity = 0;
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
    public int getTotalStoredQuantity() {
        return quantity;
    }

    @Override
    public Set<ResourceType> getStoredResourceTypes() {
        return ImmutableSet.of(resourceType);
    }
}
