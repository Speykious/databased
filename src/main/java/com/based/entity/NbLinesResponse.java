package com.based.entity;

import java.io.Serializable;

public class NbLinesResponse implements Serializable {
    private int nbLines;

    public NbLinesResponse(int nbLines) {
        this.nbLines = nbLines;
    }

    public int getNbLines() {
        return nbLines;
    }
}
