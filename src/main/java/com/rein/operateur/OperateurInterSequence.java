package com.rein.operateur;

import com.rein.transplantation.Sequence;

public abstract class OperateurInterSequence extends OperateurLocal{
    protected Sequence autreSequence;
    protected int deltaBeneficeSequence, deltaBeneficeAutreSequence;

    public OperateurInterSequence(){
        super();
        this.autreSequence = null;
    }

    protected abstract int evalDeltaBeneficeSequence();
    protected abstract int evalDeltaBeneficeAutreSequence();

    // Surcoût engendré par l'application de l'opérateur sur la solution
    @Override
    protected int evalDeltaBenefice() {
        this.deltaBeneficeSequence = this.evalDeltaBeneficeSequence();
        this.deltaBeneficeAutreSequence = this.evalDeltaBeneficeAutreSequence();
        if(this.deltaBeneficeSequence == Integer.MAX_VALUE || this.deltaBeneficeAutreSequence == Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        return this.deltaBeneficeSequence + this.deltaBeneficeAutreSequence;
    }

    public int getDeltaBeneficeSequence() {
        return deltaBeneficeSequence;
    }

    public int getDeltaBeneficeAutreSequence() {
        return deltaBeneficeAutreSequence;
    }

    @Override
    public String toString() {
        return "OperateurInterSequence{" +
                "autreSequence=" + autreSequence +
                ", deltaBeneficeSequence=" + deltaBeneficeSequence +
                ", deltaBeneficeAutreSequence=" + deltaBeneficeAutreSequence +
                '}';
    }
}
