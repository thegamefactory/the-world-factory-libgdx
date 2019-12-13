package com.tgf.twf.core.util;

/**
 * Models a runnable that is executed on completion of an action.
 */
@FunctionalInterface
public interface CompletionCallback {
    CompletionCallback IDENTITY = () -> {
    };

    void complete();
}
