package com.tgf.twf.rendering;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.Entities;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.world.Building;
import com.tgf.twf.core.world.BuildingType;
import lombok.Builder;

import java.time.Duration;

/**
 * {@link System} responsible of defining {@link Building} aspects.
 * It implements it by ensuring that each entity with a {@link Building} component is attached an up to date {@link TransparentTexture}
 * component.
 */
@Builder
public class BuildingAspectSystem implements System {
    private final TransparentTexture dirt;
    private final TransparentTexture farm;
    private final TransparentTexture field;

    public BuildingAspectSystem(
            final TransparentTexture dirt,
            final TransparentTexture farm,
            final TransparentTexture field) {
        this.dirt = dirt;
        this.farm = farm;
        this.field = field;
        Entities.allComponents(Building.class).forEach(this::attachTransparentTextureComponent);
        Entities.registerComponentEventListener(this::handle, Building.class, Component.CreationEvent.class);
        Entities.registerComponentEventListener(this::handle, Building.class, Building.ConstructedEvent.class);
    }

    @Override
    public void update(final Duration delta) {
    }

    public void handle(final Building sender, final Component.CreationEvent event) {
        attachTransparentTextureComponent(sender);
    }

    public void handle(final Building sender, final Building.ConstructedEvent event) {
        sender.getEntity().getComponent(TransparentTexture.Component.class).setTransparentTexture(getBuildingTexture(sender));
    }

    private void attachTransparentTextureComponent(final Building sender) {
        sender.getEntity().attachComponent(new TransparentTexture.Component(getBuildingTexture(sender)));
    }

    public TransparentTexture getBuildingTexture(final Building building) {
        if (!building.isConstructed()) {
            return dirt;
        }
        final BuildingType buildingType = building.getBuildingType();
        switch (buildingType) {
            case FARM:
                return farm;
            case FIELD:
                return field;
            default:
                throw new IllegalStateException("Not a valid building type: " + buildingType);
        }
    }
}
