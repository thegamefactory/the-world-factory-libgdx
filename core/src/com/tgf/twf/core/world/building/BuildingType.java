package com.tgf.twf.core.world.building;

import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.core.world.storage.Inventory;
import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.storage.Storage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

/**
 * A {@link BuildingType} defines the behavior of {@link Building} instances that are constructed from it.
 */
@RequiredArgsConstructor
public class BuildingType implements Storage.Capacity {
    public static final BuildingType FARM = new BuildingType(Rules.FARM_BUILD_DURATION, Rules.FARM_STORAGE_CAPACITY);
    public static final BuildingType FIELD = new BuildingType(Rules.FIELD_BUILD_DURATION, Rules.FIELD_STORAGE_CAPACITY);

    @Getter
    private final Duration buildTime;
    private final int foodCapacity;

    @Override
    public int getRemainingCapacity(final Inventory currentInventory, final ResourceType resourceType) {
        if (ResourceType.FOOD.equals(resourceType)) {
            return Math.max(foodCapacity - currentInventory.getStoredQuantity(resourceType), 0);
        } else {
            return 0;
        }
    }
}
