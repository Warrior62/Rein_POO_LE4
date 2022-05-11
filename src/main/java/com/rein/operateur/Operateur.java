package com.rein.operateur;

import com.rein.transplantation.Sequence;

public abstract class Operateur {
    protected Sequence sequence;
    protected int deltaCout;

    public Operateur() {
        this.sequence = null;
        this.deltaCout = Integer.MAX_VALUE;
    }

    public Operateur(Sequence sequence) {
        this();
        this.sequence = sequence;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public int getDeltaCout() {
        return deltaCout;
    }

    public boolean isMouvementRealisable(){
        if(this.getDeltaCout() == Integer.MAX_VALUE)
            return false;
        return true;
    }

    public boolean isMeilleur(Operateur op){
        if(op == null) return true;
        if(this.getDeltaCout() >= op.getDeltaCout())
            return false;
        return true;
    }

    public boolean doMouvementIfRealisable(){
        if(isMouvementRealisable())
            return this.doMouvement();
        return false;
    }

    public boolean isMouvementAmeliorant(){
        if(this.getDeltaCout() < 0) return true;
        return false;
    }

    protected abstract int evalDeltaCout();
    protected abstract boolean doMouvement();

    @Override
    public String toString() {
        return "Operateur{"
                + "\n\tsequence=" + sequence
                + "\n\tdeltaCout=" + deltaCout + '}';
    }
}
