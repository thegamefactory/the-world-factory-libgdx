package com.tgf.twf.rendering;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.google.common.collect.ImmutableMap;
import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.Entities;
import com.tgf.twf.core.ecs.System;
import com.tgf.twf.core.world.agriculture.Field;
import com.tgf.twf.core.world.agriculture.GrowingState;
import com.tgf.twf.core.world.agriculture.GrownState;
import com.tgf.twf.core.world.agriculture.UncultivatedState;
import com.tgf.twf.core.world.building.Building;
import com.tgf.twf.core.world.building.BuildingType;

import java.util.Map;

/**
 * {@link System} responsible for defining the {@link Building} textures.
 * It implements it by ensuring that each entity with a {@link Building} component is attached an up to date {@link TransparentSprite}
 * component.
 */
public class BuildingTextures {
    private final TransparentSprite dirt;
    private final TransparentSprite error;
    private final TransparentSprite farm;
    private final TransparentSprite uncultivatedFiled;
    private final TransparentSprite growingField;
    private final TransparentSprite grownField;

    private final Map<Class<? extends Field.State>, TransparentSprite> fieldSpriteMap;

    public BuildingTextures(final TextureAtlas textureAtlas) {
        this.dirt = new TransparentSprite(textureAtlas.createSprite("dirt_tile"));
        this.error = new TransparentSprite(textureAtlas.createSprite("error_tile"));
        this.farm = new TransparentSprite(textureAtlas.createSprite("farm_tile"));
        this.uncultivatedFiled = new TransparentSprite(textureAtlas.createSprite("field_uncultivated_tile"));
        this.growingField = new TransparentSprite(textureAtlas.createSprite("field_growing_tile"));
        this.grownField = new TransparentSprite(textureAtlas.createSprite("field_grown_tile"));

        fieldSpriteMap = ImmutableMap.of(
                UncultivatedState.class, uncultivatedFiled,
                GrowingState.class, growingField,
                GrownState.class, grownField);

        Entities.allComponents(Building.class).forEach(this::attachTransparentTextureComponent);
        Entities.registerComponentEventListener(this::handle, Building.class, Component.CreationEvent.class);
        Entities.registerComponentEventListener(this::handle, Building.class, Building.ConstructedEvent.class);
        Entities.registerComponentEventListener(this::handle, Field.class, Field.StateChangeEvent.class);
    }

    public TransparentSprite getBuildingTextureForPreview(final BuildingType buildingType) {
        if (buildingType.equals(BuildingType.FARMHOUSE)) {
            return farm;
        }
        if (buildingType.equals(BuildingType.FIELD)) {
            return grownField;
        }
        return error;
    }

    private void handle(final Building sender, final Component.CreationEvent event) {
        attachTransparentTextureComponent(sender);
    }

    private void handle(final Building sender, final Building.ConstructedEvent event) {
        sender.getEntity().getComponent(TransparentSprite.Component.class).setTransparentSprite(getBuildingTexture(sender));
    }

    private void handle(final Field sender, final Field.StateChangeEvent stateChangeEvent) {
        sender.getEntity().getComponent(TransparentSprite.Component.class).setTransparentSprite(getFieldTexture(sender));
    }

    private void attachTransparentTextureComponent(final Building sender) {
        sender.getEntity().attachComponent(new TransparentSprite.Component(getBuildingTexture(sender)));
    }

    private TransparentSprite getBuildingTexture(final Building building) {
        if (!building.isConstructed()) {
            return dirt;
        }
        return getConstructedBuildingTexture(building);
    }

    private TransparentSprite getConstructedBuildingTexture(final Building building) {
        final BuildingType buildingType = building.getBuildingType();
        if (buildingType.equals(BuildingType.FARMHOUSE)) {
            return farm;
        }
        if (buildingType.equals(BuildingType.FIELD)) {
            return getFieldTexture(building.getRelatedComponent(Field.class));
        }
        return error;
    }

    private TransparentSprite getFieldTexture(final Field field) {
        if (null == field) {
            // This is to work in preview mode.
            return grownField;
        } else {
            return fieldSpriteMap.getOrDefault(field.getState().getClass(), error);
        }
    }
}
