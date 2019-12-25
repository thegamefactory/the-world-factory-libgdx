package com.tgf.twf.core.world.storage;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * Identifies the resource types.
 */
@RequiredArgsConstructor
public enum ResourceType {
    FOOD;

    private final String name = StringUtils.capitalize(this.name().toLowerCase());

    @Override
    public String toString() {
        return name;
    }
}
