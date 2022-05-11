package com.based.distrib;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class MachineTarget {
    private int index;
    private ResteasyWebTarget target;

    public MachineTarget(int index, ResteasyWebTarget target) {
        this.index = index;
        this.target = target;
    }

    public int getIndex() {
        return index;
    }

    public ResteasyWebTarget getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "24-25-301 [" + (index + 1) + "]";
    }
}
