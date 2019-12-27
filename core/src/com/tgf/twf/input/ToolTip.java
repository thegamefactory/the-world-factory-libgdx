package com.tgf.twf.input;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.tgf.twf.core.geo.GeoMap;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.building.Building;
import com.tgf.twf.core.world.storage.Capacity;
import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.storage.Storage;
import com.tgf.twf.rendering.CoordinatesTransformer;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Optional;

/**
 * This class handles rendering of information related to {@link Building}s. This allows the player to quickly
 * find out information while hovering over {@link Building}s.
 */
@RequiredArgsConstructor
public class ToolTip {
    private final CoordinatesTransformer coordinatesTransformer;
    private final GeoMap geoMap;
    private final BitmapFont font;
    private final Vector2f screenPosition;
    @Setter
    private boolean isIntrospectionKeyModifierPressed = false;

    public void render(final Batch batch) {
        if (!isIntrospectionKeyModifierPressed) {
            return;
        }

        final Optional<Building> optionalBuilding = getBuilding();
        if (!optionalBuilding.isPresent()) {
            return;
        }

        final Vector2f renderPosition = new Vector2f();
        coordinatesTransformer.convertMouseToRender(screenPosition, renderPosition);

        final GlyphLayout layout = getToolTipText(optionalBuilding.get());
        font.draw(batch, layout,
                renderPosition.x - layout.width * 0.5f,
                renderPosition.y + layout.height);
    }

    private Optional<Building> getBuilding() {
        final Vector2 worldPosition = new Vector2();
        coordinatesTransformer.convertScreenToWorld(screenPosition, worldPosition);
        if (!geoMap.isInBounds(worldPosition)) {
            return Optional.empty();
        }
        return Optional.ofNullable(geoMap.getBuildingAt(worldPosition));
    }

    private GlyphLayout getToolTipText(final Building building) {
        final Storage storage = building.getRelatedComponent(Storage.class);
        final Capacity capacity = storage.getCapacity();

        final StringBuilder text = new StringBuilder()
                .append(building.getBuildingType().getName()).append("\n");

        for (final ResourceType storableResourceType : capacity.getStorableResourceTypes()) {
            final int totalCapacity = capacity.getTotalCapacity(storableResourceType);
            final int storedCapacity = storage.getStored(storableResourceType);
            text.append(storableResourceType.toString())
                    .append(": ")
                    .append(storedCapacity)
                    .append("/")
                    .append(totalCapacity)
                    .append("\n");
        }

        return new GlyphLayout(font, text);
    }
}
