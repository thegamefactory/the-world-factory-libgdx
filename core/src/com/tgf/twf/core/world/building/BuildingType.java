package com.tgf.twf.core.world.building;

import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.core.world.storage.Capacity;
import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.storage.SingleResourceTypeCapacity;
import lombok.Builder;
import lombok.Getter;

/**
 * A {@link BuildingType} defines the behavior of {@link Building} instances that are constructed from it.
 */
@Builder
public class BuildingType {
    public static final BuildingType FARMHOUSE = BuildingType.builder()
            .agentCapacity(Rules.MAX_AGENTS_PER_FARMHOUSE)
            .costResourceType(ResourceType.WORK_UNIT)
            .costQuantity(20)
            .capacity(new SingleResourceTypeCapacity(ResourceType.FOOD, Rules.FARM_STORAGE_CAPACITY))
            .name("Farmhouse")
            .build();

    public static final BuildingType FIELD = BuildingType.builder()
            .agentCapacity(0)
            .costResourceType(ResourceType.WORK_UNIT)
            .costQuantity(10)
            .capacity(new SingleResourceTypeCapacity(ResourceType.FOOD, Rules.FIELD_FOOD_YIELD))
            .name("Field")
            .build();
    @Getter
    private final ResourceType costResourceType;
    @Getter
    private final int costQuantity;
    @Getter
    private final Capacity capacity;
    @Getter
    private final String name;
    @Getter
    private final int agentCapacity;
}
