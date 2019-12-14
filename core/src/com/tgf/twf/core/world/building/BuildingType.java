package com.tgf.twf.core.world.building;

import com.google.common.collect.ImmutableSet;
import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.core.world.storage.Capacity;
import com.tgf.twf.core.world.storage.Inventory;
import com.tgf.twf.core.world.storage.ResourceType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Set;

/**
 * A {@link BuildingType} defines the behavior of {@link Building} instances that are constructed from it.
 */
@RequiredArgsConstructor
public class BuildingType implements Capacity {
    public static final BuildingType FARM = new BuildingType(Rules.FARM_BUILD_DURATION, Rules.FARM_STORAGE_CAPACITY, "Farm");
    public static final BuildingType FIELD = new BuildingType(Rules.FIELD_BUILD_DURATION, Rules.FIELD_STORAGE_CAPACITY, "Field");

    @Getter
    private final Duration buildTime;
    private final int foodCapacity;
    @Getter
    private final String name;

    @Override
    public int getRemainingCapacity(final Inventory currentInventory, final ResourceType resourceType) {
        if (ResourceType.FOOD.equals(resourceType)) {
            return Math.max(foodCapacity - currentInventory.getStoredQuantity(resourceType), 0);
        } else {
            return 0;
        }
    }

    @Override
    public int getTotalCapacity(final ResourceType resourceType) {
        if (ResourceType.FOOD.equals(resourceType)) {
            return foodCapacity;
        } else {
            return 0;
        }
    }

    @Override
    public Set<ResourceType> getStorableResourceTypes() {
        return ImmutableSet.of(ResourceType.FOOD);
    }
}
