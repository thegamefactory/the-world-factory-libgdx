package com.tgf.twf.core.world;

import lombok.Getter;

import java.time.Duration;

public class Building {
    @Getter
    private final BuildingType buildingType;
    private Duration buildingDurationRemaining;

    public Building(final BuildingType buildingType) {
        this.buildingType = buildingType;
        this.buildingDurationRemaining = buildingType.getBuildTime();
    }

    public void build(final Duration duration) {
        buildingDurationRemaining = buildingDurationRemaining.minus(duration);
        if (buildingDurationRemaining.isNegative()) {
            buildingDurationRemaining = Duration.ZERO;
        }
    }

    public boolean isBuilt() {
        return buildingDurationRemaining.isZero();
    }

    public double buildProgress() {
        return (double) buildingDurationRemaining.toMillis() / buildingType.getBuildTime().toMillis();
    }
}
