package com.tgf.twf.core.world.terrain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * An enumerator to represent the type of terrain of a tile.
 */
@RequiredArgsConstructor
public enum TerrainType {
    GRASS(0, "grass"),
    WATER(1, "water");

    @Getter
    private final int index;

    @Getter
    private final String name;
}
