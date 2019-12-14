package com.tgf.twf.core.world.storage;

/**
 * An interface to represent a mutable inventory of resources.
 *
 * Store, retrieve and reserve must be implemented as atomic transactions.
 * If their invocation returns true, the mutable inventory has processed the call and altered its internal state accordingly.
 * If their invocation returns false, the mutable inventory state has not processed the call and has not altered its internal state.
 */
public interface MutableInventory extends Inventory {
    boolean store(final ResourceType resourceType, final int quantity);

    boolean store(final Inventory inventory);

    boolean retrieve(final ResourceType resourceType, final int quantity);

    boolean retrieve(final Inventory inventory);

    boolean reserve(final ResourceType resourceType, final int quantity);

    boolean reserve(final Inventory inventory);

    void clear();
}
