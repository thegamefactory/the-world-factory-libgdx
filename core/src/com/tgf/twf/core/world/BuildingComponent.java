package com.tgf.twf.core.world;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.ecs.Entity;

import java.time.Duration;

public class BuildingComponent extends Component {
    private final BuildingType buildingType;
    private Duration buildingDurationRemaining;

    public BuildingComponent(final Entity entity,
                             final BuildingType buildingType) {
        super(entity);
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
