package com.rein.solution;

import com.rein.transplantation.Sequence;
import java.util.LinkedHashSet;


public class sequencesPossibles {

    private LinkedHashSet<Sequence> cycles;
    private LinkedHashSet<Sequence> chaines;

    /**
     * Constructeur par défaut
     * */
    public void sequencesPossibles() {
        this.cycles = new LinkedHashSet<Sequence>();
        this.chaines = new LinkedHashSet<Sequence>();
    }

    //////////////////////////////////////////////////////////////
    public LinkedHashSet<Sequence> getChaines() {
        return chaines;
    }

    public LinkedHashSet<Sequence> getCycles() {
        return cycles;
    }

    public void setChaines(LinkedHashSet<Sequence> chaines) {
        this.chaines = chaines;
    }

    public void setCycles(LinkedHashSet<Sequence> cycles) {
        this.cycles = cycles;
    }
    //////////////////////////////////////////////////////////////
}
