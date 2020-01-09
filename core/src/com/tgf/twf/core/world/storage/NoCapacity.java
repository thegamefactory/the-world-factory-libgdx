package com.tgf.twf.core.world.storage;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * A capacity of 0 resources.
 */
public class NoCapacity implements Capacity {
    private final NoOpMutableInventory noOpMutableInventory = new NoOpMutableInventory();

    private NoCapacity() {

    }

    public static NoCapacity INSTANCE = new NoCapacity();

    @Override
    public int getRemainingCapacity(final Inventory currentInventory, final ResourceType resourceType) {
        return 0;
    }

    @Override
    public int getTotalCapacity(final ResourceType resourceType) {
        return 0;
    }

    @Override
    public Set<ResourceType> getStorableResourceTypes() {
        return ImmutableSet.of();
    }

    @Override
    public MutableInventory buildMutableInventory() {
        return noOpMutableInventory;
    }

    private static class NoOpMutableInventory implements MutableInventory {
        @Override
        public boolean store(final ResourceType resourceType, final int quantity) {
            return false;
        }

        @Override
        public boolean retrieve(final ResourceType resourceType, final int quantity) {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public int getStoredQuantity(final ResourceType resourceType) {
            return 0;
        }

        @Override
        public int getTotalStoredQuantity() {
            return 0;
        }

        @Override
        public Set<ResourceType> getStoredResourceTypes() {
            return ImmutableSet.of();
        }
    }
}
