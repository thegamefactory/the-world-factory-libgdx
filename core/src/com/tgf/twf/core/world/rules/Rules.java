package com.tgf.twf.core.world.rules;

import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.storage.Storage;
import com.tgf.twf.core.world.task.Action;

import java.time.Duration;

public class Rules {
    public static final int AGENT_NANOS_PER_TILE = 1_000_000_000;
    public static final int AGENT_STORAGE_CAPACITY = 5;
    public static final Storage.Inventory CONSTRUCT_COST = Action.Cost.ONE_FOOD;
    public static final Duration FARM_BUILD_DURATION = Duration.ofSeconds(5);
    public static final int FARM_STORAGE_CAPACITY = 20;
    public static final Duration FIELD_BUILD_DURATION = Duration.ofSeconds(2);
    public static final Duration FIELD_GROWING_DURATION = Duration.ofSeconds(2);
    public static final int FIELD_STORAGE_CAPACITY = 5;
    public static final int FIELD_YIELD = 5;
    public static final Storage.Inventory HARVEST_COST = Action.Cost.ONE_FOOD;
    public static final Duration HARVEST_DURATION = Duration.ofSeconds(2);
    public static final int INITIAL_AGENT_COUNT = 2;
    public static final int INITIAL_FOOD_STORAGE = 3;
    public static final Storage.Inventory PLANT_COST = Action.Cost.ONE_FOOD;
    public static final Duration PLANT_DURATION = Duration.ofSeconds(2);
    public static final Vector2 WORLD_SIZE = new Vector2(15, 15);
}
