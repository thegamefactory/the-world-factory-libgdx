package com.tgf.twf.core.world;

import lombok.Getter;

import java.time.Duration;

/**
 * A component state modeling a live instance of a {@link BuildingType}.
 */
public class Building {
    @Getter
    private final BuildingType buildingType;
    private Duration buildingDurationRemaining;

    public Building(final BuildingType buildingType) {
        this.buildingType = buildingType;
        this.buildingDurationRemaining = buildingType.getBuildTime();
    }

    public void build(final Duration delta) {
        buildingDurationRemaining = buildingDurationRemaining.minus(delta);
        if (buildingDurationRemaining.isNegative()) {
            buildingDurationRemaining = Duration.ZERO;
        }
    }

    public boolean isBuilt() {
        return buildingDurationRemaining.isZero();
    }

    public void setBuilt() {
        buildingDurationRemaining = Duration.ZERO;
    }
}
