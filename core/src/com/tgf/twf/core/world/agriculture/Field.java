package com.tgf.twf.core.world.agriculture;

import com.tgf.twf.core.ecs.Component;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;

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

    public interface State {
        Class<? extends State> tick(Duration delta);

        void onStateEnter();
    }

    /**
     * An event emitted when the {@link Field.State} of a {@link Field} changes.
     */
    public static final class StateChangeEvent implements Component.Event {
        public static final StateChangeEvent INSTANCE = new StateChangeEvent();
    }
}
