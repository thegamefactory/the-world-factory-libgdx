package com.tgf.twf.core.world.storage;

/**
 * An interface to represent a mutable inventory of resources.
 */
public interface MutableInventory extends Inventory {
    void store(final ResourceType resourceType, final int quantity);

    void store(final Inventory inventory);

    void transfer(final Storage destinationStorage);

    void retrieve(final ResourceType resourceType, final int quantity);

    void clear();
}
