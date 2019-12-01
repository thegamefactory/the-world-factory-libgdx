package com.tgf.twf.core.ecs;

public interface ComponentStateUpdateListener<StateT> {
    void onComponentStateUpdated(final Component<StateT> component, final StateT oldState);
}
