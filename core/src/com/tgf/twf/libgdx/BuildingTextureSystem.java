package com.tgf.twf.libgdx;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.ComponentLifecycleListener;
import com.tgf.twf.core.ecs.Entities;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.world.Building;
import com.tgf.twf.core.world.BuildingType;
import lombok.Builder;

import java.time.Duration;

@Builder
public class BuildingTextureSystem implements System, ComponentLifecycleListener<Building> {
    private final TransparentTexture dirt;
    private final TransparentTexture farm;
    private final TransparentTexture field;

    public BuildingTextureSystem(
            final TransparentTexture dirt,
            final TransparentTexture farm,
            final TransparentTexture field) {
        this.dirt = dirt;
        this.farm = farm;
        this.field = field;
        Entities.allComponents(Building.class).forEach(this::onComponentAttached);
    }

    @Override
    public void update(final Duration delta) {
        // if this class was listening to building state events, it would not have to update the texture of all buildings at each frame
        Entities.allComponents(Building.class).forEach(buildingStateComponent ->
                buildingStateComponent
                        .getRelatedComponent(TransparentTexture.class)
                        .updateState(getBuildingTexture(buildingStateComponent.getState()))
        );
    }

    @Override
    public void onComponentAttached(final Component<Building> component) {
        component.getEntity().attachComponent(getBuildingTexture(component.getState()));
    }

    public TransparentTexture getBuildingTexture(final Building building) {
        final BuildingType buildingType = building.getBuildingType();
        switch (buildingType) {
            case FARM:
                return farm;
            case FIELD:
                return building.isBuilt() ? field : dirt;
            default:
                throw new IllegalStateException("Not a valid building type: " + buildingType);
        }
    }
}
