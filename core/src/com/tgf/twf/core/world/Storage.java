package com.tgf.twf.core.world;

import com.tgf.twf.core.ecs.Component;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * A component to model a capacity to store resources and the actual quantity of stored resources.
 * The capacity of the storage is modeled in {@link StorageCapacity}.
 */
@RequiredArgsConstructor
public class Storage extends Component {
    private final Map<ResourceType, Integer> storedItems = new HashMap<>();
    private final StorageCapacity storageCapacity;

    public int getStoredQuantity(final ResourceType resourceType) {
        return storedItems.getOrDefault(resourceType, 0);
    }

    /**
     * @param offeredQuantity The quantity offered to the storage.
     * @param resourceType    The type of resource offered to the storage.
     * @return The number of stored resource. Will be equals to the quantity parameter if the storage capacity allows.
     */
    public int store(final ResourceType resourceType, final int offeredQuantity) {
        int stored = 0;
        final int remainingCapacity = storageCapacity.getRemainingCapacity(this, resourceType);
        if (remainingCapacity > 0) {
            stored = Math.min(remainingCapacity, offeredQuantity);
            storedItems.put(resourceType, getStoredQuantity(resourceType) + stored);
        }
        return stored;
    }

    /**
     * @param resources The quantity of resources to consume, per resource type.
     * @return true if the storage had enough resources in store to satisfy the demand. If true, the resources are removed from the storage. If
     * false, the storage is left untouched.
     */
    public boolean tryConsumeResources(final Map<ResourceType, Integer> resources) {
        final boolean canAllocate = resources.entrySet().stream().allMatch(c -> getStoredQuantity(c.getKey()) >= c.getValue());
        if (canAllocate) {
            resources.forEach(this::forceConsume);
        }
        return canAllocate;
    }

    private void forceConsume(final ResourceType resourceType, final int requestedQuantity) {
        storedItems.put(resourceType, getStoredQuantity(resourceType) - requestedQuantity);
    }
}
