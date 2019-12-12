package com.tgf.twf.core.world.terrain;

/**
 * An interface to alter a {@link TerrainMap}.
 */
public interface TerrainGenerator {
    void generate(final TerrainMap terrainMap);
}
