package com.tgf.twf.core.world;

import lombok.Data;

@Data
public class Resource {
    private final ResourceType resourceType;
    private final int quantity;
}
