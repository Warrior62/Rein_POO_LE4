/**
 *
 * @author Mathis
 * Project : rein-poo-le4
 * Situation in transplantation
 * Description : Séquence class
 */

package com.rein.transplantation;

import com.rein.instance.Noeud;

import java.util.ArrayList;

public abstract class Sequence {

    private int id;
    private int benefMedicalSequence = 0;
    private ArrayList<Noeud> listeNoeuds;
    private int tailleMaxSequence;

    public void increaseBenefMedicalTotal(int nb){
        this.benefMedicalSequence += nb;
    }

    public void decreaseBenefMedicalTotal(int nb){
        this.benefMedicalSequence -= nb;
    }

    public int getId() {
        return id;
    }

    public int getBenefMedicalTotal() {
        return benefMedicalSequence;
    }

    public void setBenefMedicalTotal(int benefMedicalTotal) {
        this.benefMedicalSequence = benefMedicalTotal;
    }

    public ArrayList<Noeud> getListeNoeuds() {
        return listeNoeuds;
    }

    @Override
    public String toString() {
        String noeuds = "";
        for(Noeud n : listeNoeuds)
            noeuds += n.getId() + " ";
        return "Situation {" +
                "benefMedicalTotal=" + benefMedicalSequence +
                ", listeIdNoeuds=[" + noeuds + "]}";
    }
}
