package com.based.entity.dto;

import java.io.Serializable;

public class NodePingDTO implements Serializable {
    private int nodeIndex;

    public NodePingDTO(int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + nodeIndex;
        return result;
    }
}
