package com.tgf.twf.core.world;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
public enum BuildingType {
    FARM(Duration.ofSeconds(10)),
    FIELD(Duration.ofSeconds(5));

    @Getter
    private final Duration buildTime;
}
