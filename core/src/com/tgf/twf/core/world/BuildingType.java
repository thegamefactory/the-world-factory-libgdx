package com.tgf.twf.core.world;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
public enum BuildingType {
    FARM(Duration.ofSeconds(5)),
    FIELD(Duration.ofSeconds(10));

    @Getter
    private final Duration buildTime;
}
