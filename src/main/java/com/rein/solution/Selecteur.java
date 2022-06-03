package com.rein.solution;

import com.rein.transplantation.Sequence;

import java.util.LinkedHashSet;

public class Selecteur {

    private final SequencesPossibles sequencesPossibles;

    public Selecteur(SequencesPossibles sequencesPossibles) {
        this.sequencesPossibles = sequencesPossibles;
    }

    public LinkedHashSet<Sequence> getSequencesParBenefice() {
        for(Sequence cyclePossible : this.sequencesPossibles.getCycles()){

        }
        for(Sequence chainePossible : this.sequencesPossibles.getChaines()){
            System.out.println(chainePossible);
        }
        return null;
    }

    /*public LinkedHashSet<Sequence> getSequencesParSociabilite() {

    }

    public LinkedHashSet<Sequence> getSequencesAleatoirement() {

    }

    public LinkedHashSet<Sequence> getSequencesParRacineAleatoire() {

    }

    public LinkedHashSet<Sequence> getSequencesParScoreRegret() {

    }

    public LinkedHashSet<Sequence> getSequencesParSimplex() {

    }*/
}
