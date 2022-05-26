package com.rein.operateur;

import com.rein.instance.Noeud;
import com.rein.transplantation.Sequence;

public class InsertionNoeud extends Operateur{

    private Noeud noeud;
    private int position;

    public InsertionNoeud() {
        super();
        this.position = 0;
    }

    public InsertionNoeud(Sequence sequence, Noeud noeud, int position) {
        super(sequence);
        this.noeud = noeud;
        this.position = position;
        this.deltaBeneficeMedical = this.evalDeltaBenefice();
    }

    public Noeud getNoeud() {
        return noeud;
    }

    public int getPosition() {
        return position;
    }

    @Override
    protected int evalDeltaBenefice() {
        /*if(sequence == null) return Integer.MAX_VALUE;
        return this.sequence.deltaCoutInsertion(position, client);*/
        return 0;
    }

    @Override
    protected boolean doMouvement() {
        return this.sequence.doInsertion(this);
    }

    @Override
    public String toString() {
        return "InsertionNoeud{"
                + "noeud=" + noeud
                + ", position=" + position
                + ", sequence=" + super.sequence.toString()
                + ", deltaBenefice=" + super.deltaBeneficeMedical + '}';
    }
}
