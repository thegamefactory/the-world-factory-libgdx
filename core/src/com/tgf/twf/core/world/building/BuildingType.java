package com.tgf.twf.core.world.building;

import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.core.world.storage.Capacity;
import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.storage.SingleResourceTypeCapacity;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;

/**
 * A {@link BuildingType} defines the behavior of {@link Building} instances that are constructed from it.
 */
@Builder
public class BuildingType {
    public static final BuildingType FARM = BuildingType.builder()
            .buildTime(Rules.FARM_BUILD_DURATION)
            .capacity(new SingleResourceTypeCapacity(ResourceType.FOOD, Rules.FARM_STORAGE_CAPACITY))
            .name("Farm")
            .build();
    public static final BuildingType FIELD = BuildingType.builder()
            .buildTime(Rules.FIELD_BUILD_DURATION)
            .capacity(new SingleResourceTypeCapacity(ResourceType.FOOD, Rules.FIELD_STORAGE_CAPACITY))
            .name("Field")
            .build();
    @Getter
    private final Duration buildTime;
    @Getter
    private final Capacity capacity;
    @Getter
    private final String name;
}
