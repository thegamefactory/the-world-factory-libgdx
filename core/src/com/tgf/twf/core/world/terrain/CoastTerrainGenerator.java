package com.tgf.twf.core.world.terrain;

import com.tgf.twf.core.geo.Vector2;

/**
 * A simple {@link TerrainGenerator} which generates a straight coast at the edge of the map.
 */
public class CoastTerrainGenerator implements TerrainGenerator {
    @Override
    public void generate(final TerrainMap terrainMap) {
        final int sizeX = terrainMap.getSize().x;
        final Vector2 position = new Vector2();
        for (position.x = 0; position.x < sizeX; position.x++) {
            terrainMap.setTerrainAt(position, TerrainType.WATER);
        }
    }
}
