package com.rein.operateur;

public class IntraDeplacement extends OperateurIntraSequence {

    public IntraDeplacement(){
        super();
    }

    @Override
    protected int evalDeltaBenefice() {
        /*if(this.sequence == null) return Integer.MAX_VALUE;
        return this.sequence.deltaCoutDeplacement(this.positionI, this.positionJ);*/
        return 0;
    }

    @Override
    protected boolean doMouvement() {
        /*if(this.sequence == null) return false;
        return this.sequence.doDeplacement(this);*/
        return false;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
