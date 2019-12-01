package com.tgf.twf.core.world.task;

import com.tgf.twf.core.world.AgentComponent;

import java.util.List;

@FunctionalInterface
public interface TaskActionGenerator {
    List<Action> createActions(final AgentComponent agentComponent);
}
