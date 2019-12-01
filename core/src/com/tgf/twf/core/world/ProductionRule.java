package com.tgf.twf.core.world;

import lombok.Data;

import java.time.Duration;
import java.util.List;

@Data
public class ProductionRule {
    private final List<Resource> input;
    private final List<Resource> output;
    private final Duration duration;
}
