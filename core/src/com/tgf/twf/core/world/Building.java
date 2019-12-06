package com.tgf.twf.core.world;

import com.tgf.twf.core.ecs.Component;
import lombok.Getter;

import java.time.Duration;

/**
 * A component state modeling a live instance of a {@link BuildingType}.
 */
public class Building extends Component {
    @Getter
    private final BuildingType buildingType;
    private Duration buildingDurationRemaining;
    private BuildingState buildingState = BuildingState.CONSTRUCTING;

    public enum BuildingState {
        CONSTRUCTING,
        CONSTRUCTED
    }

    public Building(final BuildingType buildingType) {
        this.buildingType = buildingType;
        this.buildingDurationRemaining = buildingType.getBuildTime();
    }

    public void build(final Duration delta) {
        if (!isConstructed()) {
            buildingDurationRemaining = safeMinus(buildingDurationRemaining, delta);
            if (buildingDurationRemaining.isZero()) {
                setConstructed();
            }
        }
    }

    private Duration safeMinus(final Duration buildingDurationRemaining, final Duration delta) {
        final Duration result = buildingDurationRemaining.minus(delta);
        if (result.isNegative()) {
            return Duration.ZERO;
        }
        return result;
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

    public static final class ConstructedEvent implements Component.Event {
        private ConstructedEvent() {

        }

        private static final ConstructedEvent INSTANCE = new ConstructedEvent();
    }
}
