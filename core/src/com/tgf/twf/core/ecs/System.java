package com.tgf.twf.core.ecs;

import java.time.Duration;

public interface System {
    void update(Duration delta);
}
