package com.rein.solution;

import com.rein.instance.Noeud;
import com.rein.transplantation.Cycle;
import com.rein.transplantation.Sequence;

import java.util.Iterator;
import java.util.LinkedHashSet;


public class SequencesPossibles {

    private LinkedHashSet<Sequence> cycles;
    private LinkedHashSet<Sequence> chaines;
    private LinkedHashSet<Integer> noeudsUtilises;
    private int benefTotal; //arbre

    /**
     * Constructeur par défaut
     * */
    public SequencesPossibles() {
        this.cycles = new LinkedHashSet<>();
        this.chaines = new LinkedHashSet<>();
        this.noeudsUtilises = new LinkedHashSet<Integer>();
        this.benefTotal = 0;
    }

    /**
     * Constructeur par copie
     * */
    public SequencesPossibles(SequencesPossibles sp) {
        this.cycles = sp.getCycles();
        this.chaines = sp.getChaines();
        this.noeudsUtilises = sp.getNoeudsUtilises();
        this.benefTotal = sp.getBenefTotal();
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

    public int getBenefTotal() {
        return benefTotal;
    }

    public void setChaines(LinkedHashSet<Sequence> chaines) {
        this.chaines = chaines;
    }

    public void setCycles(LinkedHashSet<Sequence> cycles) {
        this.cycles = cycles;
    }

    public void setBenefTotal(int benefTotal) {
        this.benefTotal = benefTotal;
    }

    //////////////////////////////////////////////////////////////

    // Méthode chargée d'ajouter une séquence dans un objet SequencesPossibles
    public void ajouterSequence(Sequence s) {
        if (s instanceof Chaine)
            this.getChaines().add(s);
        if (s instanceof Cycle)
            this.getCycles().add(s);

        for (Noeud n : s.getListeNoeuds())
            this.getNoeudsUtilises().add(n.getId());

        this.setBenefTotal(this.getBenefTotal() + s.getBenefMedicalSequence());
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
            chaine += "[" + c.getListeIdNoeuds() + "] : ";
            chaine += c.getBenefMedicalSequence() + "\n";
            benefTotal += c.getBenefMedicalSequence();
        }

        //Chaines
        chaine += "---------- Chaines ---------- \n";
        it = this.chaines.iterator();
        while (it.hasNext()) {
            Chaine ch = (Chaine) it.next();
            chaine += "[" + ch.getListeIdNoeuds() + "] : ";
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
