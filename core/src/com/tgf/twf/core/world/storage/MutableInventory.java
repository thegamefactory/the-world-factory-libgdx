package com.tgf.twf.core.world.storage;

/**
 * An interface to represent a mutable inventory of resources.
 */
public interface MutableInventory extends Inventory {
    boolean store(final ResourceType resourceType, final int quantity);

    boolean store(final Inventory inventory);

    boolean retrieve(final ResourceType resourceType, final int quantity);

    void clear();
}
