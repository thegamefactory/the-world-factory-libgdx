package com.tgf.twf.core.world.agriculture;

import com.tgf.twf.core.ecs.Component;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
@AllArgsConstructor
public class Field extends Component {
    private State state;

    public interface State {
        Class<? extends State> tick(Duration delta);

        void onStateEnter();
    }
}
