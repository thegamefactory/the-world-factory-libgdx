package com.tgf.twf.core.world.agents;

import com.tgf.twf.core.geo.GeoMap;
import com.tgf.twf.core.world.daytimesystem.DaytimeSystem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Global game context passed to {@link AgentState#tick(Agent, AgentStateTickContext)}.
 */
@RequiredArgsConstructor
public class AgentStateTickContext {
    @Getter
    private final AgentSystem agentSystem;

    @Getter
    private final DaytimeSystem daytimeSystem;

    @Getter
    private final GeoMap geoMap;

    public boolean isNight() {
        return daytimeSystem.isNight();
    }

    public boolean isDay() {
        return daytimeSystem.isDay();
    }
}
