package com.rein.operateur;

import com.rein.transplantation.Sequence;

public abstract class Operateur {

    protected Sequence sequence;
    protected int deltaBeneficeMedical;

    public Operateur() {
        this.sequence = null;
        this.deltaBeneficeMedical = Integer.MAX_VALUE;
    }

    public Operateur(Sequence sequence) {
        this();
        this.sequence = sequence;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public int getDeltaBeneficeMedical() {
        return deltaBeneficeMedical;
    }

    public boolean isMouvementRealisable(){
        if(this.getDeltaBeneficeMedical() == Integer.MAX_VALUE)
            return false;
        return true;
    }

    public boolean doMouvementIfRealisable(){
        if(isMouvementRealisable())
            return this.doMouvement();
        return false;
    }

    protected abstract int evalDeltaBenefice();
    protected abstract boolean doMouvement();

    @Override
    public String toString() {
        return "Operateur{"
                + "\n\tsequence=" + sequence
                + "\n\tdeltaCout=" + deltaBeneficeMedical + '}';
    }
}
