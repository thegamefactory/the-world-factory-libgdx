package com.tgf.twf.core.ecs;

/**
 * {@link System} are ticked at a constant rate and update the state of the world by creating {@link Entity} objects, attaching {@link Component} to
 * them and updating the state of these {@link Component}s.
 */
public interface System {
    void tick();
}
