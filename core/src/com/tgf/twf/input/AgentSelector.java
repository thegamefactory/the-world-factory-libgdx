package com.tgf.twf.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.tgf.twf.core.geo.GeoMap;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.agents.Agent;
import com.tgf.twf.rendering.CoordinatesTransformer;
import lombok.Builder;

import java.util.List;

/**
 * Selects the closest agent from the mouse when a certain combination of keys is activated and stores it in "selectedAgent".
 */
@Builder
public class AgentSelector {
    private final CoordinatesTransformer coordinatesTransformer;
    private final GeoMap geoMap;
    private final Vector2f mouseScreenPosition;
    private final BitmapFont font;
    private Agent selectedAgent;

    private static final int SELECTION_RANGE = 2;

    public void update() {
        if (!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || !Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            selectedAgent = null;
        } else {
            selectedAgent = getClosestAgent();
        }
    }

    private Agent getClosestAgent() {
        final Vector2 worldPosition = new Vector2();
        coordinatesTransformer.convertScreenToWorld(mouseScreenPosition, worldPosition);

        Agent closestAgent = null;
        double closestAgentDistance = Double.POSITIVE_INFINITY;
        final Vector2f agentScreenPosition = new Vector2f();

        final Vector2 position = new Vector2();
        for (position.x = worldPosition.x - SELECTION_RANGE; position.x < worldPosition.x + SELECTION_RANGE; ++position.x) {
            for (position.y = worldPosition.y - SELECTION_RANGE; position.y < worldPosition.y + SELECTION_RANGE; ++position.y) {
                if (!geoMap.isInBounds(worldPosition)) {
                    continue;
                }

                final List<Agent> agents = geoMap.getAgentsAt(position);

                for (final Agent agent : agents) {
                    final Vector2f nonDiscretePosition = agent.getNonDiscretePosition();
                    coordinatesTransformer.convertWorldToScreen(nonDiscretePosition, agentScreenPosition);
                    final double diffX = agentScreenPosition.x - mouseScreenPosition.x;
                    final double diffY = agentScreenPosition.y - mouseScreenPosition.y;
                    final double distanceSquaredToAgent = diffX * diffX + diffY * diffY;
                    if (distanceSquaredToAgent < closestAgentDistance) {
                        closestAgentDistance = distanceSquaredToAgent;
                        closestAgent = agent;
                    }
                }
            }
        }

        return closestAgent;
    }
}
