package com.tgf.twf.core.world;

import com.tgf.twf.core.geo.GeoMap;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.building.Building;
import com.tgf.twf.core.world.building.BuildingType;
import com.tgf.twf.core.world.building.ConstructAction;
import com.tgf.twf.core.world.terrain.TerrainType;
import com.tgf.twf.input.ExecutionMode;
import lombok.RequiredArgsConstructor;

/**
 * An world service to invoke to expressed player intentions (captured by input).
 */
@RequiredArgsConstructor
public class PlayerIntentionApi {
    private final World world;

    public boolean build(final BuildingType buildingType, final Vector2 position, final ExecutionMode executionMode) {
        final GeoMap geoMap = world.getGeoMap();
        if (!geoMap.isInBounds(position)) {
            return false;
        }
        if (geoMap.isBuildingAt(position)) {
            return false;
        }
        if (!TerrainType.GRASS.equals(geoMap.getTerrainAt(position))) {
            return false;
        }
        if (ExecutionMode.DRY_RUN.equals(executionMode)) {
            return true;
        }

        final Building building = Building.createEntity(buildingType, position);
        world.getAgentSystem().addActionLast(new ConstructAction(building));
        return true;
    }
}
