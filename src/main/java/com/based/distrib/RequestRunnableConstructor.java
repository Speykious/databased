package com.based.distrib;

public interface RequestRunnableConstructor<T extends RequestRunnable> {
    public T construct(MachineTarget machineTarget, int requestIndex);
}
