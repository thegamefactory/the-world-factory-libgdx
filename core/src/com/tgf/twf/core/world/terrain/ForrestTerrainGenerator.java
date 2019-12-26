package com.tgf.twf.core.world.terrain;

import com.tgf.twf.core.geo.Vector2;
import lombok.RequiredArgsConstructor;

import java.util.Random;

@RequiredArgsConstructor
public class ForrestTerrainGenerator implements TerrainGenerator {
    private final Random random;
    private final float forrestRatio;

    @Override
    public void generate(final TerrainMap terrainMap) {
        final Vector2 pos = new Vector2();
        for (pos.x = 0; pos.x < terrainMap.getSize().x; pos.x++) {
            for (pos.y = 0; pos.y < terrainMap.getSize().y; pos.y++) {
                if (terrainMap.getTerrainAt(pos).equals(TerrainType.GRASS) && random.nextFloat() < forrestRatio) {
                    terrainMap.setTerrainAt(pos, TerrainType.FORREST);
                }
            }
        }
    }
}
