package com.tgf.twf.core.ecs;

import java.time.Duration;

/**
 * {@link System} are ticked on each frame and update the state of the world by creating {@link Entity} objects, attaching {@link Component} to
 * them and updating the state of these {@link Component}s.
 */
public interface System {
    void update(Duration delta);
}
