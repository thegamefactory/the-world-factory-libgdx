package com.tgf.twf.core.world.terrain;

import com.tgf.twf.core.geo.Vector2;

/**
 * A rectangular tile map with {@link TerrainType} accessors for each tile.
 */
public interface TerrainMap {
    Vector2 getSize();

    TerrainType getTerrainAt(final Vector2 position);

    void setTerrainAt(final Vector2 position, final TerrainType terrainType);
}
