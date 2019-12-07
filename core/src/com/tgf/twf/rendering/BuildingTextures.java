package com.tgf.twf.rendering;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.Entities;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.world.Building;
import com.tgf.twf.core.world.BuildingType;

/**
 * {@link System} responsible for defining the {@link Building} textures.
 * It implements it by ensuring that each entity with a {@link Building} component is attached an up to date {@link TransparentSprite}
 * component.
 */
public class BuildingTextures implements Disposable {
    private final TransparentSprite dirt;
    private final TransparentSprite farm;
    private final TransparentSprite field;

    public BuildingTextures(final TextureAtlas textureAtlas) {
        this.dirt = new TransparentSprite(textureAtlas.createSprite("dirt_tile"));
        this.farm = new TransparentSprite(textureAtlas.createSprite("farm_tile"));
        this.field = new TransparentSprite(textureAtlas.createSprite("field_tile"));

        Entities.allComponents(Building.class).forEach(this::attachTransparentTextureComponent);
        Entities.registerComponentEventListener(this::handle, Building.class, Component.CreationEvent.class);
        Entities.registerComponentEventListener(this::handle, Building.class, Building.ConstructedEvent.class);
    }

    public void handle(final Building sender, final Component.CreationEvent event) {
        attachTransparentTextureComponent(sender);
    }

    public void handle(final Building sender, final Building.ConstructedEvent event) {
        sender.getEntity().getComponent(TransparentSprite.Component.class).setTransparentSprite(getBuildingTexture(sender));
    }

    private void attachTransparentTextureComponent(final Building sender) {
        sender.getEntity().attachComponent(new TransparentSprite.Component(getBuildingTexture(sender)));
    }

    public TransparentSprite getBuildingTexture(final Building building) {
        if (!building.isConstructed()) {
            return dirt;
        }
        return getBuildingTexture(building.getBuildingType());
    }

    public TransparentSprite getBuildingTexture(final BuildingType buildingType) {
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
