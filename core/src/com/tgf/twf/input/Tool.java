package com.tgf.twf.input;

import com.tgf.twf.core.geo.Position;

/**
 * Defines an interface for player actions which can be executed at the given position. These are typically mutations to the world.
 */
@FunctionalInterface
public interface Tool {
    boolean execute(Position worldPosition);
}
