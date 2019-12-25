package com.tgf.twf.core.world.storage;

import com.google.common.collect.ImmutableSet;
import lombok.ToString;

import java.util.Set;

/**
 * Inventory for a mutable quantity of a single {@link ResourceType}.
 */
@ToString
public class SingleResourceTypeInventory implements MutableInventory {
    private final ResourceType resourceType;
    private int quantity;

    public SingleResourceTypeInventory(final ResourceType resourceType, final int quantity) {
        this.resourceType = resourceType;
        this.quantity = quantity;
    }

    public static SingleResourceTypeInventory empty(final ResourceType resourceType) {
        return new SingleResourceTypeInventory(resourceType, 0);
    }

    @Override
    public boolean store(final ResourceType resourceType, final int quantity) {
        if (quantity == 0) {
            return true;
        }
        if (resourceType == this.resourceType) {
            this.quantity += quantity;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean retrieve(final ResourceType resourceType, final int quantity) {
        if (quantity == 0) {
            return true;
        }
        if (resourceType != this.resourceType) {
            return false;
        }
        if (quantity <= this.quantity) {
            this.quantity -= quantity;
            return true;
        } else {
            return false;
        }
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
    public int getTotalStoredQuantity() {
        return quantity;
    }

    @Override
    public Set<ResourceType> getStoredResourceTypes() {
        return ImmutableSet.of(resourceType);
    }
}
