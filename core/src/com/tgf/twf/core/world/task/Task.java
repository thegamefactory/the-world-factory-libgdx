package com.tgf.twf.core.world.task;

import com.tgf.twf.core.ecs.Component;
import com.tgf.twf.core.world.AgentState;

import java.util.List;

public interface Task {
    List<Action> createActions(final Component<AgentState> agentStateComponent);
}
