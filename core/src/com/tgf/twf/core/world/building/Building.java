package com.tgf.twf.core.world.building;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.Entity;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.storage.Storage;
import lombok.Getter;

/**
 * A component state modeling a live instance of a {@link BuildingType}.
 */
public class Building extends Component {
    @Getter
    private final BuildingType buildingType;
    private BuildingState buildingState = BuildingState.CONSTRUCTING;

    @Getter
    private final Vector2 position;

    public enum BuildingState {
        CONSTRUCTING,
        CONSTRUCTED
    }

    public Building(final BuildingType buildingType, final Vector2 position) {
        this.buildingType = buildingType;
        this.position = new Vector2(position);
    }

    public static Building createEntity(final BuildingType buildingType, final Vector2 position) {
        final Building building = new Building(buildingType, position);
        Entity.builder()
                .withComponent(building)
                .withComponent(new Storage(buildingType.getCapacity()))
                .buildAndAttach();
        return building;
    }

    public boolean isConstructed() {
        return BuildingState.CONSTRUCTED.equals(buildingState);
    }

    public void setConstructed() {
        if (!BuildingState.CONSTRUCTED.equals(buildingState)) {
            buildingState = BuildingState.CONSTRUCTED;
            notify(ConstructedEvent.INSTANCE);
        }
    }

    /**
     * An event emitted when a {@link Building} construction completes.
     */
    public static final class ConstructedEvent implements Component.Event {
        private ConstructedEvent() {

        }

        private static final ConstructedEvent INSTANCE = new ConstructedEvent();
    }
}
