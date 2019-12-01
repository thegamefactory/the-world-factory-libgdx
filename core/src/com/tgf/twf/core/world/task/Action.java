package com.tgf.twf.core.world.task;

import java.time.Duration;

public interface Action {
    boolean isComplete();

    void update(final Duration delta);
}
