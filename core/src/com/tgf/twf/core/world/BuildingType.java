package com.tgf.twf.core.world;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

/**
 * A {@link BuildingType} defines the behavior of {@link Building} instances that are constructed from it.
 */
@RequiredArgsConstructor
public enum BuildingType {
    FARM(Duration.ofSeconds(10)),
    FIELD(Duration.ofSeconds(5));

    @Getter
    private final Duration buildTime;
}
