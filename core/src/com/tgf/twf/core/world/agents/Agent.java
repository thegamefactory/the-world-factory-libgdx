package com.tgf.twf.core.world.agents;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.geo.Vector2f;
import com.tgf.twf.core.pathfinding.PathWalker;
import com.tgf.twf.core.world.building.Building;
import com.tgf.twf.core.world.home.Home;
import com.tgf.twf.core.world.rules.Rules;
import com.tgf.twf.core.world.storage.ResourceType;
import com.tgf.twf.core.world.storage.Storage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * {@link Agent}s perform {@link Action}s to tick the game.
 * To perform an {@link Action}, an {@link Agent} requires energy.
 * Energy can be obtained either fast by converting food to energy or slowly by waiting in the idle state.
 * Agents act according to a state machine of {@link AgentState}s.
 * Each state has a {@link AgentState#tick(Agent, AgentStateTickContext)} method that returns the next state if there's a need for state transition.
 * In the {@link IdleAgentState}, the agent will first try to satisfy its food needs, then to store full resources that it's carrying, and then to
 * pick up a new action to perform.
 * An agent has a home which is a {@link Building} it's attached to and it's trying to go to to store accumulated resources and retrieved food.
 * This is meant to evolve in the future with the concept of day night cycle.
 */
public class Agent extends Component {
    private final Home home;

    @Getter
    @Setter
    private AgentState state = IdleAgentState.INSTANCE;

    /**
     * The {@link Action} that the agent is trying to accomplish. Can be null. The agent can also be busy with other things such as trying to get
     * food from home and only resume executing the action later.
     */
    @Getter
    @Setter
    private Action action;

    @Getter
    private final Vector2 position;

    /**
     * For rendering purposes, a vector to indicate the position of the agent compared to the center of the tile. Its x and y components must remain
     * between -0.5 and +0.5.
     */
    @Getter
    private final Vector2f subTilePosition = new Vector2f();

    /**
     * If the agent is currently following a path it is stored in this object.
     */
    @Getter
    private PathWalker pathWalker;

    @Getter
    private int food = Rules.AGENT_MAX_FOOD;

    public Agent(final Home home, final Vector2 position) {
        this.home = home;
        this.position = new Vector2(position);
    }

    public void decreaseFood(final int foodRequired) {
        this.food -= foodRequired;
    }

    public Home getHome() {
        return home;
    }

    Vector2 getHomePosition() {
        return getHome().getRelatedComponent(Building.class).getPosition();
    }

    public Storage getHomeStorage() {
        return getHome().getRelatedComponent(Storage.class);
    }

    Storage getStorage() {
        return getRelatedComponent(Storage.class);
    }

    boolean isAnyStoredResourceFull() {
        return getStorage().isAnyResourceFull();
    }

    public boolean isHome() {
        return this.position.equals(getHomePosition());
    }

    public boolean isHungry() {
        return this.food < Rules.AGENT_MAX_FOOD;
    }

    public boolean isIdle() {
        return action == null && pathWalker == null;
    }

    public void increaseFood(final int foodQuantity) {
        this.food += foodQuantity;
    }

    public boolean isStarving() {
        return this.food <= 0;
    }

    boolean isStorageEmpty() {
        return getStorage().isEmpty();
    }

    public int retrieve(final ResourceType resourceType, final int quantity) {
        if (resourceType.equals(ResourceType.WORK_UNIT)) {
            return quantity;
        } else {
            return getStorage().retrieveToEmpty(resourceType, quantity);
        }
    }

    public void setPathWalker(final PathWalker pathWalker) {
        if (this.pathWalker != null && this.pathWalker != pathWalker) {
            this.pathWalker.close();
        }
        this.pathWalker = pathWalker;
    }

    public void setPosition(final Vector2 newPosition) {
        notify(new MoveEvent(newPosition));
        position.x = newPosition.x;
        position.y = newPosition.y;
    }

    int store(final ResourceType resourceType, final int quantity) {
        return getStorage().storeToCapacity(resourceType, quantity);
    }

    @RequiredArgsConstructor
    public static class MoveEvent implements Event {
        @Getter
        private final Vector2 newPosition;
    }
}
