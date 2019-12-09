package com.tgf.twf.core.world.building;

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
    public static final BuildingType FARM = new BuildingType(Duration.ofSeconds(15), 20);
    public static final BuildingType FIELD = new BuildingType(Duration.ofSeconds(15), 5);

    @Getter
    private final Duration buildTime;
    private final int foodCapacity;

    @Override
    public int getRemainingCapacity(final Storage storage, final ResourceType resourceType) {
        if (ResourceType.FOOD.equals(resourceType)) {
            return Math.max(foodCapacity - storage.getStoredQuantity(resourceType), 0);
        } else {
            return 0;
        }
    }
}
