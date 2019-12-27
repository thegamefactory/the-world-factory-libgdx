package com.tgf.twf.input;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.tgf.twf.core.geo.GeoMap;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.world.agents.Agent;
import com.tgf.twf.core.world.building.Building;
import com.tgf.twf.core.world.storage.Capacity;
import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.storage.Storage;
import com.tgf.twf.rendering.CoordinatesTransformer;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

/**
 * This class handles rendering of information related to {@link Building}s. This allows the player to quickly
 * find out information while hovering over {@link Building}s.
 */
@RequiredArgsConstructor
public class AgentSelector {
    private final CoordinatesTransformer coordinatesTransformer;
    private final GeoMap geoMap;
    // private final WorldInputListener worldInputListener; 
    private final BitmapFont font;

    @Setter
    private boolean isSnapKeyModifierPressed = true;

    public void render(final Batch batch) {
        if (!isSnapKeyModifierPressed) {
            return;
        }

        final Optional<Agent> optionalAgent = getClosestAgent();
        if (!optionalAgent.isPresent()) {
            return;
        }

        final Vector2f renderPosition = new Vector2f();
        coordinatesTransformer.convertMouseToRender(worldInputListener.getMouseScreen(), renderPosition);

        final GlyphLayout layout = getToolTipText(optionalAgent.get());
        font.draw(batch, layout,
                renderPosition.x - layout.width * 0.5f,
                renderPosition.y + layout.height);
    }

    private Optional<Agent> getClosestAgent() {
        final Vector2 worldPosition = new Vector2();
        coordinatesTransformer.convertScreenToWorld(worldInputListener.getMouseScreen(), worldPosition);
        if (!geoMap.isInBounds(worldPosition)) {
            return Optional.empty();
        }

        final List<Agent> agents = geoMap.getAgentsAt(worldPosition);

        Agent closestAgent = null;
        double closestAgentDistance = Double.POSITIVE_INFINITY;
        final Vector2f agentScreenPosition = new Vector2f();
        for (Agent agent: agents) {
            final Vector2f nonDiscretePosition = agent.getNonDiscretePosition();
            coordinatesTransformer.convertWorldToScreen(nonDiscretePosition, agentScreenPosition);
            final double diffX = agentScreenPosition.x - worldInputListener.getMouseScreen().x;
            final double diffY = agentScreenPosition.y - worldInputListener.getMouseScreen().y;
            final double distanceSquaredToAgent = diffX*diffX + diffY*diffY;
            if (distanceSquaredToAgent < closestAgentDistance) {
                closestAgentDistance = distanceSquaredToAgent;
                closestAgent = agent;
            }
        }

        return Optional.ofNullable(closestAgent);
    }

    private GlyphLayout getToolTipText(final Agent agent) {
        final Storage storage = agent.getRelatedComponent(Storage.class);
        final Capacity capacity = storage.getCapacity();

        final StringBuilder text = new StringBuilder()
                .append(agent.getEntityId()).append("\n");

        for (final ResourceType storableResourceType : capacity.getStorableResourceTypes()) {
            final int totalCapacity = capacity.getTotalCapacity(storableResourceType);
            final int storedCapacity = storage.getStored(storableResourceType);
            text.append(storableResourceType.toString())
                    .append(": ")
                    .append(storedCapacity)
                    .append("/")
                    .append(totalCapacity)
                    .append("\n");
        }

        return new GlyphLayout(font, text);
    }
}
