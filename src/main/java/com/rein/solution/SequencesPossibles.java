package com.rein.solution;

import com.rein.transplantation.Cycle;
import com.rein.transplantation.Sequence;

import java.util.Iterator;
import java.util.LinkedHashSet;


public class SequencesPossibles {

    private LinkedHashSet<Sequence> cycles;
    private LinkedHashSet<Sequence> chaines;
    private LinkedHashSet<Integer> noeudsUtilises;

    /**
     * Constructeur par défaut
     * */
    public SequencesPossibles() {
        this.cycles = new LinkedHashSet<>();
        this.chaines = new LinkedHashSet<>();
        this.noeudsUtilises = new LinkedHashSet<>();
    }

    //////////////////////////////////////////////////////////////
    public LinkedHashSet<Sequence> getChaines() {
        return chaines;
    }

    public LinkedHashSet<Sequence> getCycles() {
        return cycles;
    }

    public LinkedHashSet<Integer> getNoeudsUtilises() {
        return noeudsUtilises;
    }

    public void setChaines(LinkedHashSet<Sequence> chaines) {
        this.chaines = chaines;
    }

    public void setCycles(LinkedHashSet<Sequence> cycles) {
        this.cycles = cycles;
    }
    //////////////////////////////////////////////////////////////

    public int calculBenefTotal(){
        int benef = 0;
        for (Sequence cy: cycles){
            benef += cy.getBenefMedicalSequence();
        }
        for (Sequence ch: chaines){
            benef += ch.getBenefMedicalSequence();
        }
        return benef;
    }

    @Override
    public String toString() {
        String chaine = "";
        Iterator it;
        int benefTotal = 0;

        //Cycles
        chaine += "---------- Cycles ---------- \n";
        it = this.cycles.iterator();
        while (it.hasNext()) {
            Cycle c = (Cycle) it.next();
            chaine += "[" + c.getListeIdNoeuds() + "]\n";
            chaine += c.getBenefMedicalSequence() + "\n";
            benefTotal += c.getBenefMedicalSequence();
        }

        //Chaines
        chaine += "---------- Chaines ---------- \n";
        it = this.chaines.iterator();
        while (it.hasNext()) {
            Chaine ch = (Chaine) it.next();
            chaine += "[" + ch.getListeIdNoeuds() + "]\n";
            chaine += ch.getBenefMedicalSequence() + "\n";
            benefTotal += ch.getBenefMedicalSequence();
        }

        //Noeuds utilisés
        chaine += "---------- Noeuds utilisés ---------- \n";
        chaine += this.getNoeudsUtilises() + "\n";

        //BenefTotal
        chaine += "---------- Benef ---------- \n";
        chaine += benefTotal;


        return chaine;
    }
}
