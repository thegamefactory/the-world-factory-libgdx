package com.tgf.twf.core.world.agriculture;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.geo.Vector2;
import com.tgf.twf.core.world.building.Building;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A component which is essentially composed of a {@link State}.
 * Fields state loop from {@link UncultivatedState} to {@link GrowingState} an then {@link GrownState}.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Field extends Component {
    private State state;

    public Vector2 getPosition() {
        return getRelatedComponent(Building.class).getPosition();
    }

    public interface State {
        Class<? extends State> tick();

        void onStateEnter();
    }

    /**
     * An event emitted when the {@link Field.State} of a {@link Field} changes.
     */
    public static final class StateChangeEvent implements Component.Event {
        public static final StateChangeEvent INSTANCE = new StateChangeEvent();
    }
}
