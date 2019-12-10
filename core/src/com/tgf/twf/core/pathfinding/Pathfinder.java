package com.tgf.twf.core.pathfinding;

import com.tgf.twf.core.geo.Vector2;

/**
 * Base interface for an algorithm that can produce a {@link Path} from an origin to a target position.
 */
public interface Pathfinder {
    Path find(Vector2 origin, Vector2 target);
}
