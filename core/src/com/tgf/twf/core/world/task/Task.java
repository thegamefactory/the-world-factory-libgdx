package com.tgf.twf.core.world.task;

import com.tgf.twf.core.world.AgentComponent;

import java.util.List;

public interface Task {
    List<Action> createActions(final AgentComponent agentComponent);
}
