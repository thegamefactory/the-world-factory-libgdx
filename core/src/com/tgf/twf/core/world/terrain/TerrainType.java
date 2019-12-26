package com.tgf.twf.core.world.terrain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * An enumerator to represent the type of terrain of a tile.
 */
@RequiredArgsConstructor
public enum TerrainType {
    BERRY("berry"),
    FORREST("forrest"),
    GRASS("grass"),
    WATER("water");

    @Getter
    private final String name;
}
