package com.tgf.twf.core.world.storage;

import org.apache.commons.lang3.StringUtils;

/**
 * Identifies the resource types.
 */
public enum ResourceType {
    FOOD;

    private String name = StringUtils.capitalize(this.name().toLowerCase());

    public String toString() {
        return name;
    }
}
