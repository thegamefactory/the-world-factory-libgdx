package com.tgf.twf.core.world.home;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.agents.Agent;
import com.tgf.twf.core.world.building.Building;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * A component for a container which can host agents and spawn new agents.
 */
@RequiredArgsConstructor
public class Home extends Component {
    private final List<Agent> agents = new ArrayList<>();

    @Getter
    @Setter
    private int agentCapacity = 0;

    public void attachAgent(final Agent agent) {
        this.agents.add(agent);
    }

    public int getAgentCount() {
        return agents.size();
    }

    public Vector2 getPosition() {
        return getRelatedComponent(Building.class).getPosition();
    }
}
