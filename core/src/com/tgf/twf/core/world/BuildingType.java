package com.tgf.twf.core.world;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
public enum BuildingType {
    FARM(Duration.ofSeconds(15)),
    FIELD(Duration.ofMinutes(1));

    @Getter
    private final Duration buildTime;
}
