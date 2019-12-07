package com.tgf.twf.rendering;

import com.badlogic.gdx.utils.Disposable;
import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.Entities;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.world.Building;
import com.tgf.twf.core.world.BuildingType;
import lombok.Builder;

/**
 * {@link System} responsible for defining the {@link Building} textures.
 * It implements it by ensuring that each entity with a {@link Building} component is attached an up to date {@link TransparentTexture}
 * component.
 */
@Builder
public class BuildingTextures implements Disposable {
    private final TransparentTexture dirt = new TransparentTexture("dirt_tile.png");
    private final TransparentTexture farm = new TransparentTexture("farm_tile.png");
    private final TransparentTexture field = new TransparentTexture("field_tile.png");

    public BuildingTextures() {
        Entities.allComponents(Building.class).forEach(this::attachTransparentTextureComponent);
        Entities.registerComponentEventListener(this::handle, Building.class, Component.CreationEvent.class);
        Entities.registerComponentEventListener(this::handle, Building.class, Building.ConstructedEvent.class);
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
        return getBuildingTexture(building.getBuildingType());
    }

    public TransparentTexture getBuildingTexture(final BuildingType buildingType) {
        if (buildingType.equals(BuildingType.FARM)) {
            return farm;
        }
        if (buildingType.equals(BuildingType.FIELD)) {
            return field;
        }
        throw new IllegalStateException("Not a valid building type: " + buildingType);
    }

    @Override
    public void dispose() {
        this.field.dispose();
        this.farm.dispose();
        this.dirt.dispose();
    }
}
