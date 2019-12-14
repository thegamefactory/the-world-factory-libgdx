package com.tgf.twf.core.world.task;

import com.google.common.collect.ImmutableSet;
import com.tgf.twf.core.world.storage.Inventory;
import com.tgf.twf.core.world.storage.ResourceType;

import java.time.Duration;
import java.util.Set;

/**
 * {@link Agent} components enqueue {@link Action}s and execute them in sequence.
 */
public interface Action {
    boolean isComplete();

    void update(final Duration delta);

    Inventory getCost();

    String getName();

    final class Cost {
        public static final Inventory FREE = new Inventory() {
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
        };

        public static final Inventory ONE_FOOD = Cost.of(ResourceType.FOOD, 1);

        public static Inventory of(final ResourceType resourceType, final int quantity) {
            return new Inventory() {
                final Set<ResourceType> stored = ImmutableSet.of(resourceType);

                @Override
                public int getStoredQuantity(final ResourceType resourceType) {
                    return resourceType.equals(resourceType) ? quantity : 0;
                }

                @Override
                public int getTotalStoredQuantity() {
                    return quantity;
                }

                @Override
                public Set<ResourceType> getStoredResourceTypes() {
                    return stored;
                }
            };
        }
    }
}
