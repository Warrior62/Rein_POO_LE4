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

    private int benefMedicalTotal = 0;
    private ArrayList<Noeud> listeNoeuds;

    public void increaseBenefMedicalTotal(int nb){
        this.benefMedicalTotal += nb;
    }

    public void decreaseBenefMedicalTotal(int nb){
        this.benefMedicalTotal -= nb;
    }

    public int getBenefMedicalTotal() {
        return benefMedicalTotal;
    }

    public void setBenefMedicalTotal(int benefMedicalTotal) {
        this.benefMedicalTotal = benefMedicalTotal;
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
                "benefMedicalTotal=" + benefMedicalTotal +
                ", listeIdNoeuds=[" + noeuds + "]}";
    }
}
