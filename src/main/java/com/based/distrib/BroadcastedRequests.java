package com.based.distrib;

public final class BroadcastedRequests<T extends RequestRunnable> {
    private T[] successfulRequestRunnables;
    private T[] failedRequestRunnables;

    public BroadcastedRequests(T[] successfulRequestRunnables, T[] failedRequestRunnables) {
        this.successfulRequestRunnables = successfulRequestRunnables;
        this.failedRequestRunnables = failedRequestRunnables;
    }

    // Get the set of runnables that have responded.
    public T[] getSuccessfulRequestRunnables() {
        return successfulRequestRunnables;
    }

    // Get the set of runnables that have not responded.
    public T[] getFailedRequestRunnables() {
        return failedRequestRunnables;
    }
}
