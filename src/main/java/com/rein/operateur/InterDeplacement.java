package com.rein.operateur;

import com.rein.transplantation.Sequence;

public class InterDeplacement extends OperateurInterSequence{

    public InterDeplacement(){
        super();
    }

    public InterDeplacement(Sequence sequence, Sequence autreSequence, int positionI, int positionJ){
        super(sequence, autreSequence, positionI, positionJ);
    }

    @Override
    protected int evalDeltaBeneficeSequence() {
        /*if(this.sequence == null) return Integer.MAX_VALUE;
        return this.sequence.deltaCoutSuppression(this.positionI);*/
        return 0;
    }

    @Override
    protected int evalDeltaBeneficeAutreSequence() {
        /*if(this.autreSequence == null) return Integer.MAX_VALUE;
        return this.autreSequence.deltaCoutInsertionInter(this.positionJ, this.clientI);*/
        return 0;
    }

    @Override
    protected boolean doMouvement() {
        return this.sequence.doDeplacement(this);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
